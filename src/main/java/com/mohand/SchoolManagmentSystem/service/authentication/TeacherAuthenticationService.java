package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.service.teacher.ITeacherService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
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

        if (userService.checkIfUserExist(input.getEmail())) {
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
}
