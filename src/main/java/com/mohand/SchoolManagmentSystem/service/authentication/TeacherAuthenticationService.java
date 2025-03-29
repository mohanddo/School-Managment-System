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
import com.mohand.SchoolManagmentSystem.response.authentication.SignUpResponse;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.teacher.ITeacherService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class TeacherAuthenticationService extends AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    private final ITeacherService teacherService;

    private final AzureBlobService azureBlobService;

    public TeacherAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, ITeacherService teacherService, AzureBlobService azureBlobService, JwtService jwtService, ModelMapper modelMapper) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService, modelMapper);
        this.teacherService = teacherService;
        this.azureBlobService = azureBlobService;
    }


    @Override
    public SignUpResponse signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.getEmail())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.getPassword())) {
            throw new WeakPasswordException();
        }


            String sasToken = azureBlobService.createContainer(String.valueOf(Instant.now().getEpochSecond()));
            Teacher teacher = new Teacher(input.getFirstName(), input.getLastName(), input.getEmail(),
                    passwordEncoder.encode(input.getPassword()),
                    generateVerificationCode(),
                    LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000),
                    sasToken);
            sendVerificationEmail(teacher.getEmail(), teacher.getVerificationCode());
            teacherService.save(teacher);
            return new SignUpResponse(teacher.getVerificationCode(), teacher.getVerificationCodeExpiresAt());

    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Teacher authenticate(LogInUserRequest request) {
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

        com.mohand.SchoolManagmentSystem.response.authentication.Teacher teacherResponse = modelMapper.map(teacher, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class);
        String jwtToken = jwtService.generateToken(teacher);
        teacherResponse.setJwtToken(jwtToken);
        return teacherResponse;
    }

    @Override
    public com.mohand.SchoolManagmentSystem.response.authentication.Teacher verifyUser(VerifyUserRequest request) {
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

            System.out.println("Before Mapping: " + teacher.getSasToken());
            com.mohand.SchoolManagmentSystem.response.authentication.Teacher teacherResponse = modelMapper.map(teacher, com.mohand.SchoolManagmentSystem.response.authentication.Teacher.class);
            System.out.println("After Mapping: " + teacherResponse.getSasToken());


            String jwtToken = jwtService.generateToken(teacher);
            teacherResponse.setJwtToken(jwtToken);

            teacherService.save(teacher);
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
