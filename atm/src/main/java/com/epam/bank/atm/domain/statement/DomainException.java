package com.epam.bank.atm.domain.statement;

public class DomainException extends RuntimeException {
    private final Statement statement;

    public DomainException(Statement statement) {
        super(statement.errorMessage());
        this.statement = statement;
    }

    public Statement getStatement() {
        return this.statement;
    }
}
