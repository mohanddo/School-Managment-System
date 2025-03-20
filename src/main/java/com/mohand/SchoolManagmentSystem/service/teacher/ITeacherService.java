package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.model.Teacher;

public interface ITeacherService {

    Teacher readTeacherById(Long id);
    Teacher save(Teacher teacher);
}
