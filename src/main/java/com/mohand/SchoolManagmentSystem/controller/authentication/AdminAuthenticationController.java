package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.Admin;
import com.mohand.SchoolManagmentSystem.service.authentication.AdminAuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("${api.prefix}/auth/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {
    private final AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
        adminAuthenticationService.signup(request);
        return ResponseEntity.ok("Admin registered successfully");
    }

        @PostMapping("/verify")
        public ResponseEntity<Admin> verifyStudent(@Valid @RequestBody VerifyUserRequest request, HttpServletResponse response) {
            return ResponseEntity.ok(adminAuthenticationService.verifyUser(request, response));
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
                adminAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }


}
