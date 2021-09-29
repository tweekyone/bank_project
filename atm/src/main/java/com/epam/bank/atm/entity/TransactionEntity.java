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
public class TransactionEntity {

    private long id;
    private long sourceAccount;
    private long destinationAccount;
    private double amount;
    private Calendar dateTime;

    private enum OperationType {
        CASH,
        WITHDRAWAL,
        TRANSFER,
        PAYMENTS
    }

    private enum State {
        IN_PROCESS,
        DONE,
        CANCELLED,
        CLOSED
    }


}
