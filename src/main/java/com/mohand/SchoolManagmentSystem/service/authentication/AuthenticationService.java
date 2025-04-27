package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.User;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
public abstract class AuthenticationService {

    @Value("${verification.code.expiration-time}")
    protected Long verificationCodeExpirationTime;

    protected final IUserService userService;
    protected final PasswordEncoder passwordEncoder;
    protected final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    protected final JwtService jwtService;
    protected final ModelMapper modelMapper;

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    abstract void signup(RegisterUserRequest request);

    abstract com.mohand.SchoolManagmentSystem.response.authentication.User authenticate(LogInUserRequest request, HttpServletResponse response);

    abstract User verifyUser(VerifyUserRequest request, HttpServletResponse response);

    abstract void resendVerificationCode(String email);

    protected String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        return String.valueOf(code);
    }

    protected void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Account Verification";
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\"> VERIFICATION CODE " + verificationCode + "</p>"
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
}
