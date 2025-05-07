package com.mohand.SchoolManagmentSystem.service.student;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import com.mohand.SchoolManagmentSystem.request.user.UpdateStudentRequest;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IStudentService {
    Student getByEmail(String email);

    Student save(Student student);

    List<Student> getAllStudents();

    boolean checkIfExistByEmail(String email);

    boolean checkIfExistById(Long id);

    Student getById(Long id);

    com.mohand.SchoolManagmentSystem.response.authentication.Student me(Authentication authentication);

    void addStudentToCourse(Long courseId, Long studentId);

    void addCoursesToStudentResponse(com.mohand.SchoolManagmentSystem.response.authentication.Student studentResponse);

    void update(UpdateStudentRequest request, Long id);

    List<StudentCourse> getAllCourses(Student student);

    StudentCourse getCourseResponseById(Long courseId, Long studentId);
}
