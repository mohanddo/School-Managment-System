package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        if (userService.checkIfExist(input.getEmail())) {
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
        return studentService.save(student);
    }

    @Override
    public LoginResponse authenticate(LogInUserRequest request) {
        Student student = studentService.getByEmail(request.getEmail());

        if (!student.isEnabled()) {
            throw new AccountNotEnabledException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(student);
        return new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
    }

    @Override
    public void verifyUser(VerifyUserRequest request) {
        Student student = studentService.getByEmail(request.getEmail());

        if (student.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (student.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (student.getVerificationCode().equals(request.getVerificationCode())) {
            student.setEnabled(true);
            student.setVerificationCode(null);
            student.setVerificationCodeExpiresAt(null);
            studentService.save(student);
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        Student student = studentService.getByEmail(email);
        if (student.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }
        student.setVerificationCode(generateVerificationCode());
        student.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(student.getEmail(), student.getVerificationCode());
        studentService.save(student);
    }

}
