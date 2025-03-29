package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.SignUpResponse;
import com.mohand.SchoolManagmentSystem.response.authentication.Teacher;
import com.mohand.SchoolManagmentSystem.service.authentication.TeacherAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${api.prefix}/auth/teacher")
@RequiredArgsConstructor
public class TeacherAuthenticationController {
    private final TeacherAuthenticationService teacherAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> register(@RequestBody RegisterUserRequest request) {
            return ResponseEntity.ok(teacherAuthenticationService.signup(request));
    }

        @PostMapping("/login")
        public ResponseEntity<Teacher> authenticate(@RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(teacherAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<Teacher> verifyStudent(@RequestBody VerifyUserRequest request) {
                return ResponseEntity.ok(teacherAuthenticationService.verifyUser(request));
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
                teacherAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
