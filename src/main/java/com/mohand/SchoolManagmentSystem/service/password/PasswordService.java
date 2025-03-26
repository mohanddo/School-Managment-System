package com.mohand.SchoolManagmentSystem.service.password;

import com.mohand.SchoolManagmentSystem.exception.user.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.model.PasswordResetToken;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.PasswordResetTokenRepository;
import com.mohand.SchoolManagmentSystem.request.password.ResetPasswordRequest;
import com.mohand.SchoolManagmentSystem.service.EmailService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import com.mohand.SchoolManagmentSystem.util.Util;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final IUserService userService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void sendResetPasswordLink(String email) {
        try {
            String uuid = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForStudent(email, uuid);
            emailService.sendRestPasswordMail(email, uuid);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String redirectToChangePasswordPage(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
            if (passwordResetToken == null) {
                return "redirect:/invalidLink.html";
            } else if (passwordResetToken.hasTokenExpired()) {
                return "redirect:/linkExpired.html";
            } else {
                return "redirect:/api/v1/password/updatePassword?token=" + token;
            }
    }


    public String showUpdatePasswordPage(String token, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken.hasTokenExpired()) {
            return "redirect:/linkExpired.html";
        }
        model.addAttribute("token", token);
        return "update-password";
    }

    @Transactional
    public void savePasswordAfterResetting(ResetPasswordRequest request) {
        User user = userService.getUserByToken(request.token());

        if (!request.matchPassword().equals(request.newPassword())) {
            throw new ChangePasswordException("Passwords are not the same");
        } else if (!Util.isValidPassword(request.newPassword())) {
            throw new WeakPasswordException();
        } else {
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userService.save(user);
            passwordResetTokenRepository.deletePasswordResetTokenByToken(request.token());
        }
    }
}
