package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentAuthenticationService extends AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    private final IStudentService studentService;

    public StudentAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, IStudentService studentService, JwtService jwtService, ModelMapper modelMapper) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService, modelMapper);
        this.studentService = studentService;
    }


    public void signup(RegisterUserRequest input) {

        if (userService.checkIfExist(input.email())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.password())) {
            throw new WeakPasswordException();
        }

        Student student = new Student(input.firstName(), input.lastName(), input.email(),
                passwordEncoder.encode(input.password()),
                generateVerificationCode(),
                LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(student.getEmail(), student.getVerificationCode());
        studentService.save(student);
    }

}
