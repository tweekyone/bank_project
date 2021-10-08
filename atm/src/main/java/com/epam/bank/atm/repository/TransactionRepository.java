package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);

    Optional<Transaction> getById(long id);

    List<Transaction> getByAccountId(long accountId);
}
