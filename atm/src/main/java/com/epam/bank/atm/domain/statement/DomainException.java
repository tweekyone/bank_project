package com.epam.bank.atm.domain.statement;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}