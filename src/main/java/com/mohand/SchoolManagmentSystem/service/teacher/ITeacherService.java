package com.mohand.SchoolManagmentSystem.service.teacher;

import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ITeacherService {

    Teacher readById(Long id);
    Teacher save(Teacher teacher);
    Teacher getByEmail(String email);

    boolean checkIfExistByEmail(String email);
    boolean checkIfExistById(Long id);

    com.mohand.SchoolManagmentSystem.response.authentication.Teacher me(Authentication authentication);

    List<TeacherCourse> getAllCourses(Teacher teacher);
    TeacherCourse getCourseResponseById(Long courseId, Long teacherId);
}
