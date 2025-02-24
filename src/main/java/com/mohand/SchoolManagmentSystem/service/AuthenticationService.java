package com.mohand.SchoolManagmentSystem.service;

import com.mohand.SchoolManagmentSystem.exception.student.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.student.StudentNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.student.StudentNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.StudentAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.LogInStudentRequest;
import com.mohand.SchoolManagmentSystem.request.RegisterStudentRequest;
import com.mohand.SchoolManagmentSystem.request.VerifyStudentRequest;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IStudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    public Student signup(RegisterStudentRequest input) throws AccountAlreadyExistException {


        if (studentService.checkIfStudentExist(input.getEmail())) {
            throw new AccountAlreadyExistException("The is already an account with the email " + input.getEmail());
        }

        Student student = new Student(input.getFirstName(), input.getLastName(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        student.setVerificationCode(generateVerificationCode());
        student.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        student.setEnabled(false);
        sendVerificationEmail(student);
        return studentService.saveStudent(student);
    }

    public Student authenticate(LogInStudentRequest request) throws StudentNotFoundException, StudentNotEnabledException {
        Student student = studentService.getStudentByEmail(request.getEmail());

        if (!student.isEnabled()) {
            throw new StudentNotEnabledException("Student with email: " + request.getEmail() + " is not enabled");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return student;
    }

    public void verifyStudent(VerifyStudentRequest request) throws StudentNotFoundException, VerificationCodeExpiredException, VerificationCodeInvalidException {
        Student student = studentService.getStudentByEmail(request.getEmail());
        if (student.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (student.getVerificationCode().equals(request.getVerificationCode())) {
            student.setEnabled(true);
            student.setVerificationCode(null);
            student.setVerificationCodeExpiresAt(null);
            studentService.saveStudent(student);
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    public void resendVerificationCode(String email) throws StudentNotFoundException, StudentAlreadyVerifiedException {
        Student student = studentService.getStudentByEmail(email);
        if (student.isEnabled()) {
            throw new StudentAlreadyVerifiedException("Student with email " + email + " is already verified");
        }
        student.setVerificationCode(generateVerificationCode());
        student.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(student);
        studentService.saveStudent(student);
    }

    public void sendVerificationEmail(Student student) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + student.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(student.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 100000;
        return String.valueOf(code);
    }
}
