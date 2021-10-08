package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.exception.SqlMappingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JDBCAccountRepository implements AccountRepository {

    private final String url;
    private final String username;
    private final String password;

    public JDBCAccountRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Account getById(long accountId) {
        String sql = "SELECT number, is_default, plan, amount, user_id FROM account WHERE id = ?;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Account(
                    accountId,
                    resultSet.getDouble("number"),
                    resultSet.getBoolean("is_default"),
                    resultSet.getString("plan"),
                    resultSet.getDouble("amount"),
                    resultSet.getLong("user_id")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }

    public long putMoney(long accountId, double putAmount) {
        String sql = "UPDATE account set amount = amount + ? where id = ?;";
        return executeMoneyOperation(accountId, putAmount, sql);
    }

    public long withdrawMoney(long accountId, double withdrawAmount) {
        String sql = "UPDATE account set amount = amount - ? where id = ?;";
        return executeMoneyOperation(accountId, withdrawAmount, sql);
    }

    // returns account id if successful
    private long executeMoneyOperation(long accountId, double amount, String sql) {
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
}
