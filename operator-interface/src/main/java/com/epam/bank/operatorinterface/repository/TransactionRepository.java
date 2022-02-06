package com.epam.bank.operatorinterface.repository;

import com.epam.bank.operatorinterface.entity.Transaction;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findBySourceAccountAccountNumberOrDestinationAccountAccountNumber(
        String sourceAccountNumber,
        String destinationAccountNumber);

    List<Transaction> findBySourceAccountAccountNumberAndDateTimeOrDestinationAccountAccountNumberAndDateTime(
        String sourceAccountNumber, ZonedDateTime dateTime,
        String destinationAccountNumber, ZonedDateTime dateTime2);


    @Query(value = "SELECT id, amount, operation_type, date_time, state, source_account_number, "
        + "source_is_external, destination_account_number, destination_is_external "
        + "FROM transaction "
        + "WHERE (source_account_number = ?1 or destination_account_number = ?1) and "
        + "DATE(date_time) = ?2", nativeQuery = true)
    List<Transaction> findAccountTransactionsByDate(String accountNumber, LocalDate date);

    @Query(value = "SELECT id, amount, operation_type, date_time, state, source_account_number, "
        + "source_is_external, destination_account_number, destination_is_external "
        + "FROM transaction "
        + "WHERE (source_account_number = ?1 or destination_account_number = ?1) and "
        + "to_char(date_time, 'YYYY-MM') = ?2", nativeQuery = true)
    List<Transaction> findAccountTransactionsByYearMonth(String accountNumber, String yearMonth);

    @Query(value = "SELECT id, amount, operation_type, date_time, state, source_account_number, "
        + "source_is_external, destination_account_number, destination_is_external "
        + "FROM transaction "
        + "WHERE (source_account_number = ?1 or destination_account_number = ?1) and "
        + "DATE(date_time) BETWEEN ?2 and ?3", nativeQuery = true)
    List<Transaction> findAccountTransactionsFromToDates(String accountNumber, LocalDate from, LocalDate to);
}
