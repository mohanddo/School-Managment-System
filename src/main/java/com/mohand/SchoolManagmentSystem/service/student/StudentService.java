package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.student.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.student.password.ChangePasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.password.WeakPasswordException;
import com.mohand.SchoolManagmentSystem.exception.student.password.WrongPasswordException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import com.mohand.SchoolManagmentSystem.request.ChangePasswordRequest;
import com.mohand.SchoolManagmentSystem.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
}
