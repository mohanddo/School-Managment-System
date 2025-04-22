package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Teacher;
import com.mohand.SchoolManagmentSystem.service.authentication.TeacherAuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("${api.prefix}/auth/teacher")
@RequiredArgsConstructor
public class TeacherAuthenticationController {
    private final TeacherAuthenticationService teacherAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
            teacherAuthenticationService.signup(request);
            return ResponseEntity.ok("Teacher registered successfully");
    }


        @PostMapping("/verify")
        public ResponseEntity<Teacher> verifyTeacher(@Valid @RequestBody VerifyUserRequest request, HttpServletResponse response) {
                return ResponseEntity.ok(teacherAuthenticationService.verifyUser(request, response));
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
                teacherAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
