package com.epam.bank.atm.repo;

import com.epam.bank.atm.entity.Transaction;
import java.util.List;

/* CRUD transactions repo
 */
public class TransactionRepo {

    public TransactionRepo(Transaction transaction) {
    }

    public Transaction createTransaction(Transaction transaction) {
        return null;
    }


    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    public long deleteTransaction(long transactionId) {
        return transactionId;
    }

    public Transaction findTransaction(long transaction) {
        return null;
    }

    public List<Transaction> findAllTransactions() {
        return null;
    }

}
