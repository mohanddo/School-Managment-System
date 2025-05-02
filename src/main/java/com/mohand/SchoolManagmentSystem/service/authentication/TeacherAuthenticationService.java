package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import com.mohand.SchoolManagmentSystem.service.teacher.ITeacherService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

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

}
