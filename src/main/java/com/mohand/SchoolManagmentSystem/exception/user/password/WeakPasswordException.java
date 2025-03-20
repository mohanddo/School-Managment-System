package com.mohand.SchoolManagmentSystem.exception.user.password;

public class WeakPasswordException extends PasswordException {
    public WeakPasswordException(String message) {
        super(message);
    }
    public WeakPasswordException() { super("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number."); }
}
