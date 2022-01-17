package com.epam.bank.operatorinterface.exception;

public class AccountIsClosedException extends RuntimeException {
    public AccountIsClosedException(long accountId) {
        super(String.format("Account of id=%d is closed", accountId));
    }
}
