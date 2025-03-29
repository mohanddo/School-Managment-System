package com.mohand.SchoolManagmentSystem.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record RegisterUserRequest (
    @Email String email,
    @NotBlank(message = "Password must not be empty") String password,
    @NotBlank(message = "Request must contain first name") String firstName,
    @NotBlank(message = "Request must contain last name") String lastName
) {}
