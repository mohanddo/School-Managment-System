package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.repository.PasswordResetTokenRepository;
import com.mohand.SchoolManagmentSystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Student getByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }


    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public boolean checkIfExistByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public boolean checkIfExistById(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

}
