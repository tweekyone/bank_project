package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.AtLeastOneAccountTakePartInTransaction;
import lombok.Getter;
import lombok.NonNull;
import java.time.LocalDateTime;

@Getter
public final class Transaction {
    private Long id;
    private final Long sourceAccountId;
    private final Long destinationAccountId;
    private final double amount;
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
        double amount,
        @NonNull LocalDateTime dateTime,
        @NonNull OperationType operationType,
        @NonNull State state
    ) {
        Assertion.assertA(new AtLeastOneAccountTakePartInTransaction(sourceAccountId, destinationAccountId));

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
        Long sourceAccountId,
        Long destinationAccountId,
        double amount,
        LocalDateTime dateTime,
        OperationType operationType,
        State state
    ) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.operationType = operationType;
        this.state = state;
    }
}
