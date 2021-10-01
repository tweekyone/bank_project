package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.statement.DomainException;
import com.epam.bank.atm.domain.statement.Statement;

public class AtLeastOneAccountTakePartInTransaction extends Statement {
    private Long sourceAccountId;
    private Long destinationAccountId;

    public AtLeastOneAccountTakePartInTransaction(Long sourceAccountId, Long destinationAccountId) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
    }

    @Override
    public boolean check() {
        return this.sourceAccountId != null || this.destinationAccountId != null;
    }

    @Override
    protected DomainException exception() {
        return new NoOneAccountTakePartInTrasactionException();
    }

    public class NoOneAccountTakePartInTrasactionException extends DomainException {
        public NoOneAccountTakePartInTrasactionException() {
            super("No one account take part in transaction");
        }
    }
}
