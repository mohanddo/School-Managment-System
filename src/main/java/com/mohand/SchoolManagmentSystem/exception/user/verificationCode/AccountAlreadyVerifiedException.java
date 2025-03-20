package com.mohand.SchoolManagmentSystem.exception.user.verificationCode;

public class AccountAlreadyVerifiedException extends VerificationCodeException {
    public AccountAlreadyVerifiedException(String message) {
        super(message);
    }
    public AccountAlreadyVerifiedException() {
        super("Account already verified");
    }
}
