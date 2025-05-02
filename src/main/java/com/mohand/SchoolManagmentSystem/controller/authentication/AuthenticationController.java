package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }


    @Value("${send.cookie.over.https}")
    private String sendCookieOverHttps;

    @PostMapping("/logout")
    private void logout(HttpServletResponse response) {
        String jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build().toString();

        String isLoggedCookie = ResponseCookie.from("isLogged", "")
                .httpOnly(false)
                .secure(Boolean.parseBoolean(sendCookieOverHttps))
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build().toString();

        response.addHeader("Set-Cookie", isLoggedCookie);
        response.addHeader("Set-Cookie", jwtCookie);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LogInUserRequest request, HttpServletResponse response) {
        authenticationService.authenticate(request, response);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendEmail(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification email resent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyStudent(@Valid @RequestBody VerifyUserRequest request, HttpServletResponse response) {
        authenticationService.verifyUser(request, response);
        return ResponseEntity.ok("Verification successful");
    }
}


