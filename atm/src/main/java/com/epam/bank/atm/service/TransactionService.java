package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.entity.TransactionAccountData;
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

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepo.getByAccountNumber(accountNumber);
    }

    public Transaction createTransaction(
        TransactionAccountData sourceAccount,
        TransactionAccountData destinationAccount,
        double amount,
        Transaction.OperationType operationType,
        Transaction.State state
    ) {
        Transaction transaction = new Transaction(
            sourceAccount, destinationAccount, amount, LocalDateTime.now(), operationType, state
        );
        transactionRepo.save(transaction);
        return transaction;
    }
}
