package com.epam.bank.clientinterface.repository;

import com.epam.bank.clientinterface.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
