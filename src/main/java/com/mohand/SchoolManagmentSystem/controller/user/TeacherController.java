package com.mohand.SchoolManagmentSystem.controller.user;

import com.mohand.SchoolManagmentSystem.request.user.UpdateStudentRequest;
import com.mohand.SchoolManagmentSystem.request.user.UpdateTeacherRequest;
import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import com.mohand.SchoolManagmentSystem.service.teacher.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.mohand.SchoolManagmentSystem.response.authentication.Teacher;

import java.util.List;

@Controller
@RequestMapping("${api.prefix}/teacher")
@RequiredArgsConstructor    
public class TeacherController {
    private final TeacherService teacherService;


    @GetMapping("/me")
    public ResponseEntity<Teacher> me(Authentication authentication) {
        return ResponseEntity.ok(teacherService.me(authentication));
    }

    @GetMapping("/courses/all")
    public ResponseEntity<List<TeacherCourse>> getAllCourses(Authentication authentication) {
        com.mohand.SchoolManagmentSystem.model.user.Teacher teacher = (com.mohand.SchoolManagmentSystem.model.user.Teacher)
                authentication.getPrincipal();
        return ResponseEntity.ok(teacherService.getAllCourses(teacher));
    }

    @GetMapping("/course/byId/{courseId}")
    private ResponseEntity<TeacherCourse> getCourseById(@PathVariable Long courseId, Authentication authentication) {
        Long teacherId = ((com.mohand.SchoolManagmentSystem.model.user.Teacher) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(teacherService.getCourseResponseById(courseId, teacherId));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody @Valid UpdateTeacherRequest updateTeacherRequest, Authentication authentication) {
        com.mohand.SchoolManagmentSystem.model.user.Teacher teacher = (com.mohand.SchoolManagmentSystem.model.user.Teacher) authentication.getPrincipal();
        teacherService.update(updateTeacherRequest, teacher.getId());
        return ResponseEntity.ok("Teacher updated successfully");
    }
}
