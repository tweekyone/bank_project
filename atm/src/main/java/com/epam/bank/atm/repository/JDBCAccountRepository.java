package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.exception.SqlMappingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCAccountRepository implements AccountRepository {

    private final String url;
    private final String username;
    private final String password;

    public JDBCAccountRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // TODO
    public Account getById(long id) {
        return null;
    }


    public long putMoney(long accountId, double putAmount) {
        String sql = "UPDATE account set amount = amount + ? where id = ?;";
        return executeAccountOperation(accountId, putAmount, sql);
    }

    // returns account id if successful
    private long executeAccountOperation(long accountId, double amount, String sql) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setLong(2, accountId);
            return preparedStatement.executeLargeUpdate();

        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }


    public long withdrawMoney(long accountId, double withdrawAmount) {
        String sql = "UPDATE account set amount = amount - ? where id = ?;";
        return executeAccountOperation(accountId, withdrawAmount, sql);
    }

}
