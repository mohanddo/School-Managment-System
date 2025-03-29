package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Admin;
import com.mohand.SchoolManagmentSystem.response.authentication.SignUpResponse;
import com.mohand.SchoolManagmentSystem.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${api.prefix}/auth/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {
    private final AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> register(@Valid @RequestBody RegisterUserRequest request) {
            return ResponseEntity.ok(adminAuthenticationService.signup(request));
    }

        @PostMapping("/login")
        public ResponseEntity<Admin> authenticate(@Valid @RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(adminAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<Admin> verifyStudent(@Valid @RequestBody VerifyUserRequest request) {
            return ResponseEntity.ok(adminAuthenticationService.verifyUser(request));
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
                adminAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
