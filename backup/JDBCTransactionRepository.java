package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* CRUD transactions repo
 */
public class JDBCTransactionRepository {

    public JDBCTransactionRepository(Transaction transaction) {
    }

    // is it create transaction ?
    public void save(Transaction transaction) {
    }

    public Optional<Transaction> getById(long transactionId) {
        return null;
    }

    public List<Transaction> getByAccountId(long accountId) {
        return new ArrayList<>();
    }

    public List<Transaction> getAllTransactions() {
        return null;
    }

    // TODO is it necessary?
    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    public long deleteTransaction(long transactionId) {
        return transactionId;
    }


}
