package com.mohand.SchoolManagmentSystem.controller.authentication;

import com.mohand.SchoolManagmentSystem.request.authentication.LogInUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.RegisterUserRequest;
import com.mohand.SchoolManagmentSystem.request.authentication.VerifyUserRequest;
import com.mohand.SchoolManagmentSystem.response.authentication.SignUpResponse;
import com.mohand.SchoolManagmentSystem.response.authentication.Student;
import com.mohand.SchoolManagmentSystem.service.authentication.StudentAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/auth/student")
@RequiredArgsConstructor
public class StudentAuthenticationController {
    private final StudentAuthenticationService studentAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> register(@Valid @RequestBody RegisterUserRequest request) {
            return ResponseEntity.ok(studentAuthenticationService.signup(request));
    }

        @PostMapping("/login")
        public ResponseEntity<Student> authenticate(@Valid @RequestBody LogInUserRequest request) {
                return ResponseEntity.ok(studentAuthenticationService.authenticate(request));
        }

        @PostMapping("/verify")
        public ResponseEntity<Student> verifyStudent(@Valid @RequestBody VerifyUserRequest request) {
            return ResponseEntity.ok(studentAuthenticationService.verifyUser(request));
        }

        @PostMapping("/resend")
        public ResponseEntity<String> resendEmail(@RequestParam String email) {
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
