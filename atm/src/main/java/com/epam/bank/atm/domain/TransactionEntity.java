package com.epam.bank.atm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TransactionEntity {

    private long id;
    // private UUID id;

    private long sourceAccount;
    private long destinationAccount;
    private double amount;
    private Date dateTime;

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
