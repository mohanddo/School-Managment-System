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
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.teacher.ITeacherService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class TeacherAuthenticationService extends AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    private final ITeacherService teacherService;

    private final AzureBlobService azureBlobService;

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    public TeacherAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, ITeacherService teacherService, AzureBlobService azureBlobService, JwtService jwtService, ModelMapper modelMapper) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService, modelMapper);
        this.teacherService = teacherService;
        this.azureBlobService = azureBlobService;
    }


    @Override
    public void signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.email())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.password())) {
            throw new WeakPasswordException();
        }

            String containerName = String.valueOf(Instant.now().getEpochSecond());
            azureBlobService.createContainer(containerName);
            Teacher teacher = new Teacher(input.firstName(), input.lastName(), input.email(),
                    passwordEncoder.encode(input.password()),
                    generateVerificationCode(),
                    LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000),
                    containerName);
            sendVerificationEmail(teacher.getEmail(), teacher.getVerificationCode());
            teacherService.save(teacher);
    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Teacher authenticate(LogInUserRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.getByEmail(request.email());

        if (!teacher.isEnabled()) {
            throw new AccountNotEnabledException();
        }

        String sasToken = azureBlobService.generateSASTokenForContainer(teacher.getContainerName());
        teacher.setSasToken(sasToken);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        com.mohand.SchoolManagmentSystem.response.authentication.Teacher teacherResponse = modelMapper.map(teacher, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class);
        String jwtToken = jwtService.generateToken(teacher);

        String cookie = ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build().toString();

        response.addHeader("Set-Cookie", cookie);

        return teacherResponse;
    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Teacher verifyUser(VerifyUserRequest request, HttpServletResponse response) {
        Teacher teacher = teacherService.getByEmail(request.email());

        if (teacher.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (teacher.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        String sasToken = azureBlobService.generateSASTokenForContainer(teacher.getContainerName());
        teacher.setSasToken(sasToken);

        if (teacher.getVerificationCode().equals(request.verificationCode())) {
            teacher.setEnabled(true);
            teacher.setVerificationCode(null);
            teacher.setVerificationCodeExpiresAt(null);

            com.mohand.SchoolManagmentSystem.response.authentication.Teacher teacherResponse = modelMapper.map(teacher, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class);

            teacherService.save(teacher);

            String jwtToken = jwtService.generateToken(teacher);

            String cookie = ResponseCookie.from("token", jwtToken)
                    .httpOnly(true)
                    .secure(Boolean.parseBoolean(sendCookieOverHttps))
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build().toString();

            response.addHeader("Set-Cookie", cookie);

            return teacherResponse;
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
