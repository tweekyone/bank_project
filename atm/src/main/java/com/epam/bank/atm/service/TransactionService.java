package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.TransactionEntity;
import com.epam.bank.atm.repo.TransactionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public TransactionEntity getTransactionById(long transactionId) {
        return transactionRepo.findTransaction(transactionId);
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepo.findAllTransactions();
    }

    public TransactionEntity createTransaction(TransactionEntity transaction) {
        return transactionRepo.createTransaction(transaction);
    }

    public TransactionEntity updateTransaction(TransactionEntity transaction) {
        return transactionRepo.updateTransaction(transaction);
    }

    public long deleteTransaction(long transactionId) {
        transactionRepo.deleteTransaction(transactionId);
        return transactionId;
    }

    public void logUserActivity(TransactionEntity transaction, UserActivity activity) {
        transaction.getDateTime().getTime();
        // userActivity.setActivity(objectMapper.writeValueAsString());
    }

}
