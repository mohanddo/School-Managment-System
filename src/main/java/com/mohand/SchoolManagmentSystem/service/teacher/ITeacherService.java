package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import org.springframework.security.core.Authentication;

public interface ITeacherService {

    Teacher readById(Long id);
    Teacher save(Teacher teacher);
    Teacher getByEmail(String email);

    boolean checkIfExistByEmail(String email);
    boolean checkIfExistById(Long id);

    com.mohand.SchoolManagmentSystem.response.authentication.Teacher me(Authentication authentication);
}
