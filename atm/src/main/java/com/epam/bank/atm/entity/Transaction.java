package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.AtLeastOneAccountTakePartInTransaction;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class Transaction {
    private Long id;
    private TransactionAccountData sourceAccount;
    private TransactionAccountData destinationAccount;
    private double amount;
    private LocalDateTime dateTime;
    private final OperationType operationType;
    private final State state;

    public enum OperationType {
        INTERNAL_TRANSFER, EXTERNAL_TRANSFER, CASH, WITHDRAWAL, PAYMENTS
    }

    public enum State {
        SUCCESS, DECLINE, IN_PROCESS, DONE, CANCELLED, CLOSED
    }

    public Transaction(
        TransactionAccountData sourceAccount,
        TransactionAccountData destinationAccount,
        double amount,
        @NonNull LocalDateTime dateTime,
        @NonNull OperationType operationType,
        @NonNull State state
    ) {
        Assertion.assertA(new AtLeastOneAccountTakePartInTransaction(sourceAccount, destinationAccount));

        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.dateTime = dateTime;
        this.operationType = operationType;
        this.state = state;
    }
}
