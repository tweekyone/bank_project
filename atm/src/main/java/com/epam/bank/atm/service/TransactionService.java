package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TransactionService implements TransactionalService {

    private final TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public Optional<Transaction> getTransactionById(long transactionId) {
        return transactionRepo.getById(transactionId);
    }

    public List<Transaction> getTransactionsByAccountId(long accountId) {
        return transactionRepo.getByAccountId(accountId);
    }

    public Transaction createTransaction(
        Long sourceAccountId,
        Long destinationAccountId,
        double amount,
        Transaction.OperationType operationType,
        Transaction.State state
    ) {

        Transaction transaction = new Transaction(sourceAccountId, destinationAccountId,
            amount, LocalDateTime.now(), operationType, state);
        transactionRepo.save(transaction);
        return transaction;
    }

    public Transaction updateTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
        return transaction;
    }
}
