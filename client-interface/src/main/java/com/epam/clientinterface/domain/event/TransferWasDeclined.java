package com.epam.clientinterface.domain.event;

import com.epam.clientinterface.entity.Transaction;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TransferWasDeclined extends DomainEvent {
    private final Transaction.AccountData sourceAccount;
    private final Transaction.AccountData destinationAccount;
    private final double amount;
    private final Transaction.OperationType operationType;

    public TransferWasDeclined(
        @NonNull Transaction.AccountData sourceAccount,
        @NonNull Transaction.AccountData destinationAccount,
        double amount,
        @NonNull Transaction.OperationType operationType
    ) {
        super();
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.operationType = operationType;
    }
}
