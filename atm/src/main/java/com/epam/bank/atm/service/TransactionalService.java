package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.entity.TransactionAccountData;

public interface TransactionalService {
    Transaction createTransaction(
        TransactionAccountData sourceAccount,
        TransactionAccountData destinationAccount,
        double amount,
        Transaction.OperationType operationType,
        Transaction.State state
    );
}
