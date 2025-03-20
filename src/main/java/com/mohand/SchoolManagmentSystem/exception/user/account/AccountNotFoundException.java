package com.mohand.SchoolManagmentSystem.exception.user.account;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String message) {
        super(message);
    }
    public AccountNotFoundException() {
        super("Account not found");
    }
}
