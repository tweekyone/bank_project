package com.epam.clientinterface.controller.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountId) {
        super(String.format("Account of id=%d not found", accountId));
    }
}
