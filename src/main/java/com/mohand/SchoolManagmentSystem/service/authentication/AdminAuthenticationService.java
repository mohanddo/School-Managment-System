package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.Admin;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.admin.IAdminService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminAuthenticationService extends AuthenticationService {

    private final IAdminService adminService;

    public AdminAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, IAdminService adminService, JwtService jwtService) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService);
        this.adminService = adminService;
    }


    @Override
    public Admin signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.getEmail())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.getPassword())) {
            throw new WeakPasswordException();
        }

        Admin admin = new Admin(input.getFirstName(), input.getLastName(), input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                generateVerificationCode(),
                LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(admin.getEmail(), admin.getVerificationCode());
        return adminService.save(admin);
    }

    @Override
    public LoginResponse authenticate(LogInUserRequest request) {
        Admin admin = adminService.getByEmail(request.getEmail());

        if (!admin.isEnabled()) {
            throw new AccountNotEnabledException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(admin);
        return new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
    }

    @Override
    public void verifyUser(VerifyUserRequest request) {
        Admin admin = adminService.getByEmail(request.getEmail());

        if (admin.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (admin.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (admin.getVerificationCode().equals(request.getVerificationCode())) {
            admin.setEnabled(true);
            admin.setVerificationCode(null);
            admin.setVerificationCodeExpiresAt(null);
            adminService.save(admin);
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    @Override
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
