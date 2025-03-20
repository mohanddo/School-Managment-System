package com.mohand.SchoolManagmentSystem.exception.user.verificationCode;

public class VerificationCodeExpiredException extends VerificationCodeException {
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
