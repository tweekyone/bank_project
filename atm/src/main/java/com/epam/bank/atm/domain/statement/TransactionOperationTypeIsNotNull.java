package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.entity.Transaction;

public class TransactionOperationTypeIsNotNull extends Statement {
    private final Transaction.OperationType operationType;

    public TransactionOperationTypeIsNotNull(Transaction.OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public boolean check() {
        return this.operationType != null;
    }

    @Override
    protected DomainException exception() {
        return new TransactionOperationTypeIsNull();
    }

    public class TransactionOperationTypeIsNull extends DomainException {
        public TransactionOperationTypeIsNull() {
            super("Transaction operation type is null");
        }
    }
}
