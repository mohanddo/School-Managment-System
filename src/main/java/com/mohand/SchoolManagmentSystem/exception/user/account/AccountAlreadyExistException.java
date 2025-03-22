package com.mohand.SchoolManagmentSystem.exception.user.account;

public class AccountAlreadyExistException extends AccountException {
    public AccountAlreadyExistException() {
        super("Account already exist");
    }
}
