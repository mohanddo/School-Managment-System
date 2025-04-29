package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.user.Admin;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.User;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.admin.IAdminService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AdminAuthenticationService extends AuthenticationService {

    private final IAdminService adminService;

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    public AdminAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, IAdminService adminService, JwtService jwtService,  ModelMapper modelMapper) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService, modelMapper);
        this.adminService = adminService;
    }



    public void signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.email())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.password())) {
            throw new WeakPasswordException();
        }

        Admin admin = new Admin(input.firstName(), input.lastName(), input.email(),
                passwordEncoder.encode(input.password()),
                generateVerificationCode(),
                LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(admin.getEmail(), admin.getVerificationCode());
        adminService.save(admin);
    }

    public com.mohand.SchoolManagmentSystem.response.authentication.Admin verifyUser(VerifyUserRequest request, HttpServletResponse response) {
        Admin admin = adminService.getByEmail(request.email());

        if (admin.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (admin.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (admin.getVerificationCode().equals(request.verificationCode())) {
            admin.setEnabled(true);
            admin.setVerificationCode(null);
            admin.setVerificationCodeExpiresAt(null);

            com.mohand.SchoolManagmentSystem.response.authentication.Admin adminResponse = modelMapper.map(admin, com.mohand.SchoolManagmentSystem.response.authentication.Admin.class);

            adminService.save(admin);

            String jwtToken = jwtService.generateToken(admin);

            setJwtCookie(response, jwtToken);
            setIsLoggedCookie(response);

            return adminResponse;
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    public void resendVerificationCode(String email) {
        Admin admin = adminService.getByEmail(email);
        if (admin.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }
        admin.setVerificationCode(generateVerificationCode());
        admin.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(admin.getEmail(), admin.getVerificationCode());
        adminService.save(admin);
    }

}
