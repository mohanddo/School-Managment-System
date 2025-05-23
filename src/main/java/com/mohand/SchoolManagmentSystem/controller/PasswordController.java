package com.mohand.SchoolManagmentSystem.controller;

import com.mohand.SchoolManagmentSystem.request.password.ChangePasswordRequest;
import com.mohand.SchoolManagmentSystem.request.password.ResetPasswordRequest;
import com.mohand.SchoolManagmentSystem.service.password.PasswordService;
import com.mohand.SchoolManagmentSystem.service.student.IStudentService;
import com.mohand.SchoolManagmentSystem.service.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;

@Controller
@RequestMapping("${api.prefix}/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;
    private final IUserService userService;

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal connectedStudent) {
        userService.changePassword(request, connectedStudent);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        passwordService.sendResetPasswordLink(email);
        return ResponseEntity.ok("Email send for verification");
    }

    @GetMapping("/resetPassword")
    public String redirectToChangePasswordPage(
                                         @RequestParam("token") String token) {
        return passwordService.redirectToChangePasswordPage(token);
    }

    @GetMapping("/updatePassword")
    public String showUpdatePasswordPage(@RequestParam("token") String token, Model model) {
        return passwordService.showUpdatePasswordPage(token, model);
    }

    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordService.savePasswordAfterResetting(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
