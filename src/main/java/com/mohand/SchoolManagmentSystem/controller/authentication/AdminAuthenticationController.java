package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.model.user.Admin;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.service.authentication.AdminAuthenticationService;
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
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
            Admin registerdAdmin = adminAuthenticationService.signup(request);
            return ResponseEntity.ok(registerdAdmin);
    }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(adminAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<String> verifyStudent(@RequestBody VerifyUserRequest request) {
                adminAuthenticationService.verifyUser(request);
                return ResponseEntity.ok("Admin verified");
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
                adminAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
