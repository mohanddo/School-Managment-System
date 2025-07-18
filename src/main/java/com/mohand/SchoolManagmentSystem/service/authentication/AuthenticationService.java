package com.mohand.SchoolManagmentSystem.service.authentication;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.AccountAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.user.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${verification.code.expiration-time}")
    protected Long verificationCodeExpirationTime;

    @Value("${security.jwt.expiration-time}")
    protected Long jwtExpirationTime;

    protected final IUserService userService;
    protected final PasswordEncoder passwordEncoder;
    protected final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    protected final JwtService jwtService;
    protected final ModelMapper modelMapper;

    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    @Value("${same.site}")
    private String sameSite;

    public void authenticate(LogInUserRequest request, HttpServletResponse response) {
        User user = userService.getByEmail(request.email());

        if (!user.isEnabled()) {
            resendVerificationCode(request.email());
            throw new AccountNotEnabledException();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String jwtToken = jwtService.generateToken(user);

        setJwtCookie(response, jwtToken);
        setIsLoggedCookie(response);
    }

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

    protected void setJwtCookie(HttpServletResponse response, String jwtToken) {
        String cookie = ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ofSeconds(jwtExpirationTime / 1000))
                .build().toString();

        response.addHeader("Set-Cookie", cookie);
    }

    protected void setIsLoggedCookie(HttpServletResponse response) {
        String cookie = ResponseCookie.from("isLogged", "true")
                .httpOnly(false)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ofSeconds(jwtExpirationTime / 1000 ))
                .build().toString();

        response.addHeader("Set-Cookie", cookie);
    }

    public void resendVerificationCode(String email) {
        User user = userService.getByEmail(email);
        if (user.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusSeconds(verificationCodeExpirationTime / 1000));
        sendVerificationEmail(user.getEmail(), user.getVerificationCode());
        userService.save(user);
    }

    public void verifyUser(VerifyUserRequest request, HttpServletResponse response) {
        User user = userService.getByEmail(request.email());

        if (user.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }

        if (user.getVerificationCode().equals(request.verificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);

            userService.save(user);

            String jwtToken = jwtService.generateToken(user);

            setJwtCookie(response, jwtToken);
            setIsLoggedCookie(response);

        } else {
            throw new VerificationCodeInvalidException("Verification code is invalid");
        }
    }
}
