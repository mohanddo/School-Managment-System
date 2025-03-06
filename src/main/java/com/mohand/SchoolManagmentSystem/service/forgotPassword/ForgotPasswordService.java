package com.mohand.SchoolManagmentSystem.service.forgotPassword;

import com.mohand.SchoolManagmentSystem.exception.student.otp.InvalidOTPException;
import com.mohand.SchoolManagmentSystem.exception.student.otp.OTPExpiredException;
import com.mohand.SchoolManagmentSystem.model.ForgotPassword;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.repository.ForgotPasswordRepository;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService implements IForgotPasswordService {

    private final EmailService emailService;
    private final StudentService studentService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public void sendVerificationEmail(String email, Integer otp) {
        String subject = "OTP for reset password request";

        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + "OTP " + otp + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendEmail(email, subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(999999) + 100000;
    }

    @Override
    public void verifyEmail(String email) {
        Student student = studentService.getStudentByEmail(email);
        Integer otp = generateOTP();
        ForgotPassword forgotPassword = new ForgotPassword(otp, student);
        forgotPasswordRepository.save(forgotPassword);
        sendVerificationEmail(email, otp);
    }

    @Override
    public void verifyOTP(String email, Integer otp) {
        Student student = studentService.getStudentByEmail(email);
        ForgotPassword forgotPassword = forgotPasswordRepository
                .findForgotPasswordByOtpAndStudent(otp, student)
                .orElseThrow(() -> new InvalidOTPException("Invalid OTP"));

        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.delete(forgotPassword);
            throw new OTPExpiredException("OTP Expired");
        }
    }
}
