package com.mohand.SchoolManagmentSystem.controller.user;

import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("${api.prefix}/student")
@RequiredArgsConstructor    
public class StudentController {
    private final StudentService studentService;

    
    @GetMapping("/me")
    public ResponseEntity<Student> me(Authentication authentication) {
        return ResponseEntity.ok(studentService.me(authentication));
    }

    @GetMapping("/courses/all")
    public ResponseEntity<List<StudentCourse>> getAllCourses(Authentication authentication) {
        com.mohand.SchoolManagmentSystem.model.user.Student student = (com.mohand.SchoolManagmentSystem.model.user.Student)
        authentication.getPrincipal();
        return ResponseEntity.ok(studentService.getAllCourses(student));
    }

    @GetMapping("/course/byId/{courseId}")
    private ResponseEntity<StudentCourse> getCourseById(@PathVariable Long courseId, Authentication authentication) {
        Long studentId = ((com.mohand.SchoolManagmentSystem.model.user.Student) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(studentService.getCourseResponseById(courseId, studentId));
    }

//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody @Valid UpdateStudentRequest updateStudentRequest, Authentication authentication) {
//        Student student = (Student) authentication.getPrincipal();
//        studentService.update(updateStudentRequest, student.getId());
//        return ResponseEntity.ok("Student updated successfully");
//    }
}
