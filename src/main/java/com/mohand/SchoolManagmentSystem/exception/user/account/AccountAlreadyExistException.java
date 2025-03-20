package com.mohand.SchoolManagmentSystem.exception.user.account;

public class AccountAlreadyExistException extends AccountException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
    public AccountAlreadyExistException() {
        super("Account already exist");
    }
}
