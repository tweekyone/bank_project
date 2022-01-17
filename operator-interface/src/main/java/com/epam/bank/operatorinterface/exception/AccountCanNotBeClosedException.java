package com.epam.bank.operatorinterface.exception;

public class AccountCanNotBeClosedException extends RuntimeException {
    public AccountCanNotBeClosedException(long accountId) {
        super(String.format("Account of id=%d can not be closed", accountId));
    }
}
