package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import com.mohand.SchoolManagmentSystem.service.authentication.StudentAuthenticationService;
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
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
            Teacher registerdTeacher = teacherAuthenticationService.signup(request);
            return ResponseEntity.ok(registerdTeacher);
    }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(teacherAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<?> verifyStudent(@RequestBody VerifyUserRequest request) {
                teacherAuthenticationService.verifyUser(request);
                return ResponseEntity.ok("Teacher verified");
        }

        @PostMapping("/resend")
        public ResponseEntity<?> resendEmail(@RequestParam String email) {
                teacherAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
