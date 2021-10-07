package com.epam.bank.atm.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    public Transaction(Long sourceAccountId, Long destinationAccountId,
                       double amount, LocalDateTime dateTime,
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
    private Long sourceAccountId;
    private Long destinationAccountId;
    private double amount;
    private LocalDateTime dateTime;
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
