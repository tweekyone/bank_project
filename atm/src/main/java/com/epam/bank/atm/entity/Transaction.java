package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.AtLeastOneAccountTakePartInTransaction;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;

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
        CASH,
        WITHDRAWAL,
        TRANSFER,
        PAYMENTS
    }

    public enum State {
        IN_PROCESS,
        DONE,
        CANCELLED,
        CLOSED
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
