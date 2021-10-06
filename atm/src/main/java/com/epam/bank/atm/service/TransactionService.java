package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.JDBCTransactionRepository;
import java.util.List;
import java.util.Optional;

public class TransactionService {

    private final JDBCTransactionRepository transactionRepo;

    public TransactionService(JDBCTransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Optional<Transaction> getTransactionById(long transactionId) {
        return transactionRepo.getById(transactionId);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.getAllTransactions();
    }

    public void createTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepo.updateTransaction(transaction);
    }

    public long deleteTransaction(long transactionId) {
        transactionRepo.deleteTransaction(transactionId);
        return transactionId;
    }

}
