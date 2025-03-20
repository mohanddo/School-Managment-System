package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentAuthenticationService extends AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    private final IStudentService studentService;

    public StudentAuthenticationService(IUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, IStudentService studentService, JwtService jwtService) {
        super(userService, passwordEncoder, authenticationManager, emailService, jwtService);
        this.studentService = studentService;
    }


    @Override
    public Student signup(RegisterUserRequest input) {

        if (userService.checkIfUserExist(input.getEmail())) {
            throw new AccountAlreadyExistException();
        }

        if (!Util.isValidPassword(input.getPassword())) {
            throw new WeakPasswordException();
        }

        Student student = new Student(input.getFirstName(), input.getLastName(), input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                generateVerificationCode(),
                LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(student.getEmail(), student.getVerificationCode());
        return studentService.saveStudent(student);
    }
}
