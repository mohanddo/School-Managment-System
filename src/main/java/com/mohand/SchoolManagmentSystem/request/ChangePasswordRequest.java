package com.mohand.SchoolManagmentSystem.request;

public record ChangePasswordRequest(String currentPassword, String newPassword, String repeatPassword) {}
