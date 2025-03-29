package com.mohand.SchoolManagmentSystem.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record VerifyUserRequest (
        @Email String email,
        @NotBlank(message = "Verification code must not be empty") String verificationCode
) {}
