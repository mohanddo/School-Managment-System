package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
public abstract class AuthenticationService {

    @Value("${verification.code.expiration-time}")
    private Long verificationCodeExpirationTime;

    protected final IUserService userService;
    protected final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtService jwtService;

    abstract User signup(RegisterUserRequest request);

    public LoginResponse authenticate(LogInUserRequest request) {
        User user = userService.getByEmail(request.getEmail());

        if (!user.isEnabled()) {
            throw new AccountNotEnabledException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
    }

    public void verifyUser(VerifyUserRequest request) {
        User user = userService.getByEmail(request.getEmail());
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (user.getVerificationCode().equals(request.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userService.saveUser(user);
        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }

    public void resendVerificationCode(String email) {
        User user = userService.getByEmail(email);
        if (user.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(user.getEmail(), user.getVerificationCode());
        userService.saveUser(user);
    }

    protected String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 100000;
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
