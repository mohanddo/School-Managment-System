package com.mohand.SchoolManagmentSystem.controller.user;

import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import com.mohand.SchoolManagmentSystem.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mohand.SchoolManagmentSystem.response.authentication.Teacher;

@Controller
@RequestMapping("${api.prefix}/teacher")
@RequiredArgsConstructor    
public class TeacherController {
    private final TeacherService teacherService;


    @GetMapping("/me")
    public ResponseEntity<Teacher> me(Authentication authentication) {
        return ResponseEntity.ok(teacherService.me(authentication));
    }
}
