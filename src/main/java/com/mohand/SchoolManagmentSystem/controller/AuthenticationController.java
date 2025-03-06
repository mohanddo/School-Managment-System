package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.exception.student.account.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.student.account.AccountNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.student.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.StudentAlreadyVerifiedException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeExpiredException;
import com.mohand.SchoolManagmentSystem.exception.student.verificationCode.VerificationCodeInvalidException;
import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.request.LogInStudentRequest;
import com.mohand.SchoolManagmentSystem.request.RegisterStudentRequest;
import com.mohand.SchoolManagmentSystem.request.VerifyStudentRequest;
import com.mohand.SchoolManagmentSystem.response.LoginResponse;
import com.mohand.SchoolManagmentSystem.service.AuthenticationService;
import com.mohand.SchoolManagmentSystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterStudentRequest request) {
            Student registerdStudent = authenticationService.signup(request);
            return ResponseEntity.ok(registerdStudent);
    }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestBody LogInStudentRequest request) {

                Student authenticatedStudent = authenticationService.authenticate(request);
                String jwtToken = jwtService.generateToken(authenticatedStudent);
                LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
                return ResponseEntity.ok(loginResponse);

        }

        @PostMapping("/verify")
        public ResponseEntity<?> verifyStudent(@RequestBody VerifyStudentRequest request) {
                authenticationService.verifyStudent(request);
                return ResponseEntity.ok("Student verified");
        }

        @PostMapping("/resend")
        public ResponseEntity<?> resendEmail(@RequestParam String email) {
                authenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
        }
}
