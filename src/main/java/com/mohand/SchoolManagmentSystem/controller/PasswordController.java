package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.ChangePasswordRequest;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("${api.prefix}/password")
@RequiredArgsConstructor
public class PasswordController {

    private final IStudentService studentService;

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedStudent) {
        studentService.changePassword(request, connectedStudent);
        return ResponseEntity.ok("Password changed successfully");
    }
}
