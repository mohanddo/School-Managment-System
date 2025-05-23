package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Admin;
import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.service.authentication.StudentAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("${api.prefix}/auth/student")
@RequiredArgsConstructor
public class StudentAuthenticationController {
    private final StudentAuthenticationService studentAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
            studentAuthenticationService.signup(request);
            return ResponseEntity.ok("Student registered successfully");
    }

}
