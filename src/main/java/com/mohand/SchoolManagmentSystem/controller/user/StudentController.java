package com.mohand.SchoolManagmentSystem.controller.user;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.request.user.UpdateStudentRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.service.authentication.StudentAuthenticationService;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/student")
@RequiredArgsConstructor    
public class StudentController {
    private final StudentService studentService;

    
    @GetMapping("/me")
    public ResponseEntity<Student> me(Authentication authentication) {
        return ResponseEntity.ok(studentService.me(authentication));
    }

//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestBody @Valid UpdateStudentRequest updateStudentRequest, Authentication authentication) {
//        Student student = (Student) authentication.getPrincipal();
//        studentService.update(updateStudentRequest, student.getId());
//        return ResponseEntity.ok("Student updated successfully");
//    }
}
