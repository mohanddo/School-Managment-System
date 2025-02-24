package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.exception.student.AccountAlreadyExistException;
import com.mohand.SchoolManagmentSystem.exception.student.StudentNotEnabledException;
import com.mohand.SchoolManagmentSystem.exception.student.StudentNotFoundException;
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
import org.springframework.http.HttpStatusCode;
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
        try {
            Student registerdStudent = authenticationService.signup(request);
            return ResponseEntity.ok(registerdStudent);
        } catch (AccountAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

        @PostMapping("/login")
        public ResponseEntity<?> authenticate(@RequestBody LogInStudentRequest request) {

            try {
                Student authenticatedStudent = authenticationService.authenticate(request);
                String jwtToken = jwtService.generateToken(authenticatedStudent);
                LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpirationTime());
                return ResponseEntity.ok(loginResponse);
            } catch (StudentNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (StudentNotEnabledException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        @PostMapping("/verify")
        public ResponseEntity<?> verifyStudent(@RequestBody VerifyStudentRequest request) {
            try {
                authenticationService.verifyStudent(request);
                return ResponseEntity.ok("Student verified");
            } catch (StudentNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (VerificationCodeInvalidException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (VerificationCodeExpiredException e) {
                return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
            }
        }

        @PostMapping("/resend")
        public ResponseEntity<?> resendEmail(@RequestParam String email) {
            try {
                authenticationService.resendVerificationCode(email);
                return ResponseEntity.ok("Verification email resent successfully");
            } catch (StudentNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (StudentAlreadyVerifiedException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN   ).body(e.getMessage());
            }
        }
}
