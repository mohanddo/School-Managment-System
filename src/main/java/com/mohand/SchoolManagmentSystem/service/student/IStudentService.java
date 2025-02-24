package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.exception.student.StudentNotFoundException;
import com.mohand.SchoolManagmentSystem.model.Student;

import java.util.List;

public interface IStudentService {
    Student getStudentByEmail(String email) throws StudentNotFoundException;

    Student saveStudent(Student student);

    boolean checkIfStudentExist(String email);

    List<Student> getAllStudents();
}
