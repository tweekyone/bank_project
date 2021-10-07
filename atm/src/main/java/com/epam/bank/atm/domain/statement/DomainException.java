package com.epam.bank.atm.domain.statement;

public class DomainException extends RuntimeException {
    public DomainException(Statement statement) {
        super(statement.errorMessage());
    }
}
