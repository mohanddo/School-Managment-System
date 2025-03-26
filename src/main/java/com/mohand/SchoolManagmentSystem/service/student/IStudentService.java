package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.model.user.Student;

import java.util.List;

public interface IStudentService {
    Student getByEmail(String email);

    Student save(Student student);

    List<Student> getAllStudents();

    boolean checkIfExistByEmail(String email);

    boolean checkIfExistById(Long id);

    Student getById(Long id);
}
