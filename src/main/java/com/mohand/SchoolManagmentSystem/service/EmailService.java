package com.mohand.SchoolManagmentSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }

    @Value("${base.url}")
    private String baseUrl;

    @Value("${api.prefix}")
    private String apiPrefix;

    public void sendRestPasswordMail(String to, String token) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String link = baseUrl + apiPrefix + "/password/resetPassword?token=" + token;;

        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please follow this link to reset you password:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Link:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + link + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setTo(to);
        helper.setSubject("Reset Password");
        helper.setText(htmlMessage, true);

        emailSender.send(message);
    }
}
