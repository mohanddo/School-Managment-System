package com.mohand.SchoolManagmentSystem.exception.student.verificationCode;

public class StudentAlreadyVerifiedException extends VerificationCodeException {
    public StudentAlreadyVerifiedException(String message) {
        super(message);
    }
}
