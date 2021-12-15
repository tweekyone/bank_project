package com.epam.clientinterface.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountId) {
        super(String.format("Account of id=%d not found", accountId));
    }

    public AccountNotFoundException(long userId, String accountNumber) {
        super(String.format("User with id=%d don't have account with number=%s", userId, accountNumber));
    }
}
