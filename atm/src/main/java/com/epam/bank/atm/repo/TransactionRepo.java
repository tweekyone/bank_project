package com.epam.bank.atm.repo;

import com.epam.bank.atm.entity.TransactionEntity;
import java.util.List;

/* CRUD repo
* Read transaction and transactions
* Update transaction
* Delete transaction
*/
public class TransactionRepo {

    public TransactionRepo(TransactionEntity transactionEntity) {
    }

    public TransactionEntity createTransaction(TransactionEntity transaction) {
        return null;
    };

    public TransactionEntity updateTransaction(TransactionEntity transaction) {
        return null;
    }

    public void deleteTransaction(long transactionId) {
    }

    public TransactionEntity findTransaction(long transaction) {
        return null;
    }

    public List<TransactionEntity> findAllTransactions() {
        return null;
    }

}
