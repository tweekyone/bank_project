package com.epam.bank.clientinterface.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountId) {
        super(String.format("Account of id=%d not found", accountId));
    }
}
