package com.epam.bank.operatorinterface.exception;

public class AccountNumberGenerationTriesLimitException extends RuntimeException {
    public AccountNumberGenerationTriesLimitException() {
        super("Failed to generate account number for 1000 attempts");
    }
}
