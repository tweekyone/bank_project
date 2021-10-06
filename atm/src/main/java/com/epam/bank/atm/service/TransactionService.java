package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.TransactionRepository;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class TransactionService {

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


    // -> MIRO: вызывает void save(Transaction transaction) у TransactionRepository

    public Transaction create(long sourceAccountId, long destinationAccountId,
                              double amount, Calendar createdTime, Transaction.OperationType operationType,
                              Transaction.State state) {

        Transaction transaction = new Transaction(
            sourceAccountId, destinationAccountId, amount,
            createdTime, operationType, state);

        transactionRepo.save(transaction);
        return transaction;
    }

    public Transaction updateTransaction(Transaction transaction) {
        transactionRepo.save(transaction);
        return transaction;
    }

    // TODO is it needed ?
    // public long deleteTransaction(long transactionId) {
    //     transactionRepo.deleteTransaction(transactionId);
    //     return transactionId;
    // }

}
