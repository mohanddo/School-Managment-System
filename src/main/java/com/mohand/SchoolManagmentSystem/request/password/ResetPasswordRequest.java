package com.mohand.SchoolManagmentSystem.request.password;

public record ResetPasswordRequest(String newPassword, String matchPassword, String token) {
}
