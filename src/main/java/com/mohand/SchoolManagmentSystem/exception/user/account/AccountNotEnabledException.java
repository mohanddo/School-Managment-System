package com.mohand.SchoolManagmentSystem.exception.user.account;

public class AccountNotEnabledException extends AccountException {
    public AccountNotEnabledException() {
        super("Account not enabled");
    }
}
