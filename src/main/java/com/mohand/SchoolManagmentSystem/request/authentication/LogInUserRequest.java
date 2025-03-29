package com.mohand.SchoolManagmentSystem.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record LogInUserRequest(
        @Email String email,
        @NotBlank(message = "Password must now be empty") String password
) {}
