package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.entity.Transaction;

public class TransactionStateIsNotNull extends Statement {
    private final Transaction.State state;

    public TransactionStateIsNotNull(Transaction.State state) {
        this.state = state;
    }

    @Override
    public boolean check() {
        return this.state != null;
    }

    @Override
    protected DomainException exception() {
        return new TransactionStateIsNullException();
    }

    public class TransactionStateIsNullException extends DomainException {
        public TransactionStateIsNullException() {
            super("Transaction state is null");
        }
    }
}
