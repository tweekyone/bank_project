package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.statement.DomainException;
import com.epam.bank.atm.domain.statement.Statement;
import java.util.Date;

public class TransactionDateTimeIsNotNull extends Statement {
    private Date dateTime;

    public TransactionDateTimeIsNotNull(Date dateTime) {
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
