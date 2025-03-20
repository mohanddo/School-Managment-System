package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.authentication.StudentAuthenticationService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.mohand.SchoolManagmentSystem.model.User;

@Controller
@RequestMapping("${api.prefix}/auth/student")
@RequiredArgsConstructor
public class StudentAuthenticationController {
    private final StudentAuthenticationService studentAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
            Student registerdStudent = studentAuthenticationService.signup(request);
            return ResponseEntity.ok(registerdStudent);
    }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(studentAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<?> verifyStudent(@RequestBody VerifyUserRequest request) {
                studentAuthenticationService.verifyUser(request);
                return ResponseEntity.ok("Student verified");
        }

        @PostMapping("/resend")
        public ResponseEntity<?> resendEmail(@RequestParam String email) {
                studentAuthenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
//    @Autowired
//    private TokenService tokenService;
//
//    @GetMapping("/token")
//    public ResponseEntity<OAuth2AccessToken> getToken(OAuth2AuthenticationToken authentication) {
//        OAuth2AccessToken accessToken = tokenService.getAccessToken(authentication);
//        return ResponseEntity.ok(accessToken);
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<?> getUser(OAuth2AuthenticationToken authentication) {
//        return ResponseEntity.ok(authentication.getPrincipal().getAttributes()); // Returns user info
//    }
//
//    @GetMapping("/id")
//    public ResponseEntity<?> getId(OAuth2AuthenticationToken authentication) {
//        return ResponseEntity.ok(tokenService.getIdToken(authentication)); // Returns user info
//    }
}
