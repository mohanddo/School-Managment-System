package com.mohand.SchoolManagmentSystem.request.password;

public record ChangePasswordRequest(String currentPassword, String newPassword, String repeatPassword) {}
