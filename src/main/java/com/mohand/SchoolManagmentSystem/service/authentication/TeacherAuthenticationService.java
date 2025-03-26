package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.teacher.ITeacherService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TeacherAuthenticationService extends AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    private final ITeacherService teacherService;

    public TeacherAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, ITeacherService teacherService, JwtService jwtService) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService);
        this.teacherService = teacherService;
    }


    @Override
    public Teacher signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.getEmail())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.getPassword())) {
            throw new WeakPasswordException();
        }

        Teacher teacher = new Teacher(input.getFirstName(), input.getLastName(), input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                generateVerificationCode(),
                LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(teacher.getEmail(), teacher.getVerificationCode());
        return teacherService.save(teacher);
    }

    @Override
    public LoginResponse authenticate(LogInUserRequest request) {
        Teacher teacher = teacherService.getByEmail(request.getEmail());

        if (!teacher.isEnabled()) {
            throw new AccountNotEnabledException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(teacher);
        return new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
    }

    @Override
    public void verifyUser(VerifyUserRequest request) {
        Teacher teacher = teacherService.getByEmail(request.getEmail());

        if (teacher.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (teacher.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (teacher.getVerificationCode().equals(request.getVerificationCode())) {
            teacher.setEnabled(true);
            teacher.setVerificationCode(null);
            teacher.setVerificationCodeExpiresAt(null);
            teacherService.save(teacher);
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        Teacher teacher = teacherService.getByEmail(email);
        if (teacher.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }
        teacher.setVerificationCode(generateVerificationCode());
        teacher.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(teacher.getEmail(), teacher.getVerificationCode());
        teacherService.save(teacher);
    }
}
