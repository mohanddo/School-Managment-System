package com.mohand.SchoolManagmentSystem.request.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest(@NotNull String currentPassword,
                                    @NotNull String newPassword,
                                    @NotNull String repeatPassword) {}
