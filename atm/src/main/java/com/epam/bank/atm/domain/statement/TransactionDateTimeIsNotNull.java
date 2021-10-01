package com.epam.bank.atm.domain.statement;

import java.time.LocalDateTime;

public class TransactionDateTimeIsNotNull extends Statement {
    private final LocalDateTime dateTime;

    public TransactionDateTimeIsNotNull(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean check() {
        return this.dateTime != null;
    }

    @Override
    protected DomainException exception() {
        return new TransactionDateTimeIsNullException();
    }

    public class TransactionDateTimeIsNullException extends DomainException {
        public TransactionDateTimeIsNullException() {
            super("Transaction datetime is null");
        }
    }
}
