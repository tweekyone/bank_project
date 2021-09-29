package com.epam.bank.atm.service;

import com.epam.bank.atm.repo.TransactionRepo;

public class TransactionService {

 private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }
}
