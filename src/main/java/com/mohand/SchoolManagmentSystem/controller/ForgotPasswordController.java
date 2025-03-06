package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.model.Student;
import com.mohand.SchoolManagmentSystem.service.forgotPassword.IForgotPasswordService;
import com.mohand.SchoolManagmentSystem.service.student.StudentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("${api.prefix}/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final IForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok("Email send for verification");
    }

    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
        forgotPasswordService.verifyOTP(email, otp);
        return ResponseEntity.ok("OTP Verified");
    }

//    @PostMapping("/changePassword/{email}")
//    public ResponseEntity<String> changePassword(@PathVariable )

}
