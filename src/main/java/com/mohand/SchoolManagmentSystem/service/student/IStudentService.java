package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.password.ChangePasswordRequest;

import java.security.Principal;
import java.util.List;

public interface IStudentService {
    Student getByEmail(String email);

    Student save(Student student);

    boolean checkIfStudentExist(String email);

    List<Student> getAllStudents();

//    void changePassword(ChangePasswordRequest request, Principal connectedStudent);

    void createPasswordResetTokenForStudent(String email, String token);

    Student getStudentByToken(String token);

    boolean checkIfExistByEmail(String email);
}
