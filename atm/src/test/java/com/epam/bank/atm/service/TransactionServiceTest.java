package com.epam.bank.atm.service;

import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.infrastructure.persistence.JDBCTransactionRepository;
import com.epam.bank.atm.repository.TransactionRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionServiceTest {

    private Long id = 5L;
    private final Long sourceAccountId = 3L;
    private final Long destinationAccountId = 5L;
    private final double amount = 101;
    private final LocalDateTime dateTime = LocalDateTime.of(2021, 9, 11, 12, 5, 23);
    private final Transaction.OperationType operationType = Transaction.OperationType.CASH;
    private final Transaction.State state = Transaction.State.IN_PROCESS;

    String URL = "jdbc:postgresql://localhost:5432/database";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "123qwe";
    private AuthService authService;

    Transaction transaction = new Transaction(id, sourceAccountId, destinationAccountId, amount, dateTime, operationType, state);
    Transaction transaction2 = new Transaction(6L, sourceAccountId, sourceAccountId, 500.00, LocalDateTime.of(2021, 10, 11, 22, 10 , 23),
        Transaction.OperationType.WITHDRAWAL, Transaction.State.DONE);

    private List<Transaction> transactions = new LinkedList<>();

    // private Transaction transaction ;

    // @BeforeEach
    // public void setup() throws SQLException {
    //     // var connection = DIContainer.instance().getSingleton(Connection.class);
    //     Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    //     var query = "update account set amount = 5678.58 where id = 1";
    //     connection.prepareStatement(query).executeUpdate();
    //     // authService.login("1", "123");
    // }

    // Connection connection = DIContainer.instance().getSingleton(Connection.class);
    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    private final TransactionRepository transactionRepository = new JDBCTransactionRepository(connection);
    TransactionService transactionService = new TransactionService(transactionRepository);

    public TransactionServiceTest() throws SQLException {
    }

    @Test
    void getTransactionById() {
        Assertions.assertEquals(transaction.hashCode(),
            transactionService.getTransactionById(15L).get().hashCode());
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
