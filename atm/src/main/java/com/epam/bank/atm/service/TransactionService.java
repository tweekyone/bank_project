package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Activity;
import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repo.ActivityRepo;
import com.epam.bank.atm.repo.TransactionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class TransactionService {

    private final TransactionRepo transactionRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionService(TransactionRepo transactionRepo, ActivityRepo activityRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Transaction getTransactionById(long transactionId) {
        return transactionRepo.findTransaction(transactionId);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAllTransactions();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepo.createTransaction(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepo.updateTransaction(transaction);
    }

    public long deleteTransaction(long transactionId) {
        transactionRepo.deleteTransaction(transactionId);
        return transactionId;
    }

}
