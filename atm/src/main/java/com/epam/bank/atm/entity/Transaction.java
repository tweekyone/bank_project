package com.epam.bank.atm.entity;

import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    public Transaction(long sourceAccountId, long destinationAccountId,
                       double amount, Calendar dateTime,
                       Transaction.OperationType operationType,
                       Transaction.State state) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.operationType = operationType;
        this.state = state;

    }

    private long id;
    private long sourceAccountId;
    private long destinationAccountId;
    private double amount;
    private Calendar dateTime;
    private OperationType operationType;
    private State state;

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


}
