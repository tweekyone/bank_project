package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.AtLeastOneAccountTakePartInTransaction;
import com.epam.bank.atm.domain.statement.TransactionDateTimeIsNotNull;
import com.epam.bank.atm.domain.statement.TransactionOperationTypeIsNotNull;
import com.epam.bank.atm.domain.statement.TransactionStateIsNotNull;
import java.time.LocalDateTime;

public final class Transaction {
    private Long id;
    private final Long sourceAccountId;
    private final Long destinationAccountId;
    private final long amount;
    private final LocalDateTime dateTime;
    private final OperationType operationType;
    private final State state;

    public enum OperationType {
        VALUE
    }

    public enum State {
        VALUE
    }

    public Transaction(
        Long sourceAccountId,
        Long destinationAccountId,
        long amount,
        LocalDateTime dateTime,
        OperationType operationType,
        State state
    ) {
        Assertion.assertA(new AtLeastOneAccountTakePartInTransaction(sourceAccountId, destinationAccountId));
        Assertion.assertA(new TransactionDateTimeIsNotNull(dateTime));
        Assertion.assertA(new TransactionOperationTypeIsNotNull(operationType));
        Assertion.assertA(new TransactionStateIsNotNull(state));

        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.operationType = operationType;
        this.state = state;
    }

    // Constructor for hydration
    public Transaction(
        long id,
        long sourceAccountId,
        long destinationAccountId,
        long amount,
        LocalDateTime dateTime,
        OperationType operationType,
        State state
    ) {
        this(sourceAccountId, destinationAccountId, amount, dateTime, operationType, state);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public State getState() {
        return state;
    }
}
