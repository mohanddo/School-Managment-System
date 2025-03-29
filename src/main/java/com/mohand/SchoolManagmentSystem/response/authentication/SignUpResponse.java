package com.mohand.SchoolManagmentSystem.response.authentication;

import java.time.LocalDateTime;

public record SignUpResponse(String verificationCode, LocalDateTime verificationCodeExpiresAt) {
}
