package com.mohand.SchoolManagmentSystem.service.user;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.user.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WrongPasswordException;
import com.mohand.SchoolManagmentSystem.model.PasswordResetToken;
import com.mohand.SchoolManagmentSystem.model.user.User;
import com.mohand.SchoolManagmentSystem.repository.PasswordResetTokenRepository;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import com.mohand.SchoolManagmentSystem.request.password.ChangePasswordRequest;
import com.mohand.SchoolManagmentSystem.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public boolean checkIfExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var principal = ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var user = (User) principal;

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong Password");
        }

        if (!request.newPassword().equals(request.repeatPassword())) {
            throw new ChangePasswordException("Passwords are not the same");
        }

        if (!Util.isValidPassword(request.newPassword())) {
            throw new WeakPasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

    @Value("${password.reset.token.expiration-time}")
    private Long passwordResetTokenExpirationTime;

    @Override
    public void createPasswordResetTokenForStudent(String email, String token) {
        User user = getByEmail(email);

        PasswordResetToken existingToken = user.getPasswordResetToken();

        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plusSeconds(passwordResetTokenExpirationTime / 1000));
        } else {
            existingToken = new PasswordResetToken(token, user, LocalDateTime.now().plusSeconds(passwordResetTokenExpirationTime / 1000));
        }

        passwordResetTokenRepository.save(existingToken);
    }


    @Override
    public User getUserByToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return userRepository.findByPasswordResetToken(passwordResetToken).orElseThrow(AccountNotFoundException::new);
    }

}
