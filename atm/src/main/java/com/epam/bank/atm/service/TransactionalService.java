package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;

public interface TransactionalService {
    Transaction createTransaction(
        Long sourceAccountId,
        Long destinationAccountId,
        double amount,
        Transaction.OperationType operationType,
        Transaction.State state
    );
}
