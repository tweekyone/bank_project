package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.exception.SqlMappingException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAccountRepository implements AccountRepository {

    private final Connection connection;

    public JdbcAccountRepository(Connection connection) {
        this.connection = connection;
    }

    public Account getById(long accountId) {
        String sql = "SELECT number, is_default, plan, amount, user_id FROM account WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    public double putMoney(long accountId, double putAmount) {
        String sql = "UPDATE account set amount = amount + ? where id = ?;";
        return this.getById(executeMoneyOperation(accountId, putAmount, sql)).getAmount();
    }

    public double withdrawMoney(long accountId, double withdrawAmount) {
        String sql = "UPDATE account set amount = amount - ? where id = ?;";
        return this.getById(executeMoneyOperation(accountId, withdrawAmount, sql)).getAmount();
    }

    @Override
    public double getCurrentAmount(long id) {
        String sql = "select amount from account where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            var resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("amount");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigInteger getAccountNumberById(long id) {
        return null;
    }

    // returns account id if successful
    private long executeMoneyOperation(long accountId, double amount, String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setLong(2, accountId);
            return preparedStatement.executeLargeUpdate();
        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }
}
