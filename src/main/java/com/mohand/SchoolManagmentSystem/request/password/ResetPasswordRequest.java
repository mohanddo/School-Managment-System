package com.mohand.SchoolManagmentSystem.request.password;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(@NotNull String newPassword,
                                   @NotNull String matchPassword,
                                   @NotNull String token) {
}
