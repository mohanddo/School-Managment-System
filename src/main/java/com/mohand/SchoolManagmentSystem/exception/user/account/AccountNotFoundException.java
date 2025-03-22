package com.mohand.SchoolManagmentSystem.exception.user.account;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException() {
        super("Account not found");
    }
}
