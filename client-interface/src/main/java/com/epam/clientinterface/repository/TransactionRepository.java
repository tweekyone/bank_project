package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
