package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.TransactionRepository;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TransactionServiceTest {

    //test transactions data
    private Long id = 5L;
    private final Long sourceAccountId = 3L;
    private final Long destinationAccountId = 5L;
    private final double amount = 101;
    private final LocalDateTime dateTime = LocalDateTime.of(2021, 9, 11, 12, 5, 23);
    private final Transaction.OperationType operationType = Transaction.OperationType.CASH;
    private final Transaction.State state = Transaction.State.IN_PROCESS;

    private final Transaction transaction = new Transaction(id, sourceAccountId, destinationAccountId, amount, dateTime, operationType, state);
    private final Transaction transaction2 = new Transaction(6L, sourceAccountId, sourceAccountId, 500.00, LocalDateTime.of(2021, 10, 11, 22, 10 , 23),
        Transaction.OperationType.WITHDRAWAL, Transaction.State.DONE);
    private final List<Transaction> transactions = new LinkedList<>();

    TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    private final TransactionService transactionService = new TransactionService(transactionRepository);

    @Test
    void getTransactionById() {
        Assertions.assertEquals(transaction.hashCode(),
            transactionService.getTransactionById(id).get().hashCode());
    }

    @Test
    void getTransactionsByAccountId() {
        transactions.add(transaction);
        transactions.add(transaction2);
        Assertions.assertEquals(transactions,
            transactionService.getTransactionsByAccountId(sourceAccountId));
    }

    @Test
    void create() {
        transactionService.createTransaction(sourceAccountId, destinationAccountId, amount, operationType, state);
        Assertions.assertEquals(transactionService.getTransactionById(++id).hashCode(),
            transactionRepository.getById(id).get().hashCode());
    }
}
