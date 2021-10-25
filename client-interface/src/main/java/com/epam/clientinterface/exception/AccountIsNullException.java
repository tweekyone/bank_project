package com.epam.clientinterface.exception;

public class AccountIsNullException extends RuntimeException {
    public AccountIsNullException() {
        super(String.format("Account is null"));
    }
}
