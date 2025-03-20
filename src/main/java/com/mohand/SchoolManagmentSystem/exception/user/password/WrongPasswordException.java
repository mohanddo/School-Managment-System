package com.mohand.SchoolManagmentSystem.exception.user.password;

public class WrongPasswordException extends PasswordException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
