package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("FROM Transaction WHERE source_account_number = ?1 AND source_is_external = false "
        + "OR destination_account_number = ?1 AND destination_is_external = false ORDER BY date_time DESC")
    List<Transaction> getTransactionsByNumber(String accountNumber);
}
