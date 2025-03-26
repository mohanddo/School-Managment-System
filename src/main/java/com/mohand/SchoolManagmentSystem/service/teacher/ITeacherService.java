package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.model.user.Teacher;

public interface ITeacherService {

    Teacher readById(Long id);
    Teacher save(Teacher teacher);
    Teacher getByEmail(String email);

    boolean checkIfExistByEmail(String email);
    boolean checkIfExistById(Long id);
}
