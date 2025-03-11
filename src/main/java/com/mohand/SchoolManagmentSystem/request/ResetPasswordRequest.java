package com.mohand.SchoolManagmentSystem.request;

public record ResetPasswordRequest(String newPassword, String matchPassword, String token) {
}
