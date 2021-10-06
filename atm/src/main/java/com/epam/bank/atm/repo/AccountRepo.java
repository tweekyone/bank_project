package com.epam.bank.atm.repo;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.exception.SqlMappingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class AccountRepo {

    private final String HOST;

    public AccountRepo(String host) {
        HOST = host;
    }


    public long putMoney(long accountId, long putAmount) {
        String sql = "UPDATE account set amount = amount + ? where id = ?;";
        try (Connection connection = DriverManager.getConnection(HOST);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, putAmount);
            preparedStatement.setLong(2, accountId);
            preparedStatement.execute();
            return preparedStatement.executeLargeUpdate();

        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }


    public long withdrawMoney(long accountId, long withdrawAmount) {
        String sql = "UPDATE account set amount = amount - ? where id = ?;";
        try (Connection connection = DriverManager.getConnection(HOST);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, withdrawAmount);
            preparedStatement.setLong(2, accountId);
            preparedStatement.execute();
            return preparedStatement.executeLargeUpdate();

        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }

}
