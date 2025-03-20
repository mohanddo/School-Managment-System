package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.user.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.user.password.WrongPasswordException;
import com.mohand.SchoolManagmentSystem.model.PasswordResetToken;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.repository.PasswordResetTokenRepository;
import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import com.mohand.SchoolManagmentSystem.request.password.ChangePasswordRequest;
import com.mohand.SchoolManagmentSystem.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("No student with this email: " + email));
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public boolean checkIfStudentExist(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedStudent) {
        var principal = ((UsernamePasswordAuthenticationToken) connectedStudent).getPrincipal();
        var student = ( Student ) principal;

        if (!passwordEncoder.matches(request.currentPassword(), student.getPassword())) {
            throw new WrongPasswordException("Wrong Password");
        }

        if (!request.newPassword().equals(request.repeatPassword())) {
            throw new ChangePasswordException("Passwords are not the same");
        }

        if (!Util.isValidPassword(request.newPassword())) {
            throw new WeakPasswordException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
        }

        student.setPassword(passwordEncoder.encode(request.newPassword()));

        studentRepository.save(student);
    }

    @Value("${password.reset.token.expiration-time}")
    private Long passwordResetTokenExpirationTime;

    @Override
    public void createPasswordResetTokenForStudent(String email, String token) {
        Student student = getStudentByEmail(email);

        PasswordResetToken existingToken = student.getPasswordResetToken();

        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plusSeconds(passwordResetTokenExpirationTime / 1000));
        } else {
            existingToken = new PasswordResetToken(token, student, LocalDateTime.now().plusSeconds(passwordResetTokenExpirationTime / 1000));
        }

        passwordResetTokenRepository.save(existingToken);
    }

    @Override
    public Student getStudentByToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return studentRepository.findByPasswordResetToken(passwordResetToken).orElseThrow(() -> new AccountNotFoundException("No student with this token"));
    }


}
