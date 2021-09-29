package com.epam.bank.atm.repo;

import com.epam.bank.atm.domain.TransactionEntity;

public class TransactionRepo {

    final TransactionEntity transactionEntity;

    public TransactionRepo(TransactionEntity transactionEntity) {
        this.transactionEntity = transactionEntity;
    }
}
