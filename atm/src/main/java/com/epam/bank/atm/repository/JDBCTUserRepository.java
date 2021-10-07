package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTUserRepository implements UserRepository{

    private final Connection connection;

    public JDBCTUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getById(long id) {
        String query = "select * from user where id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("phone_number"),
                    User.Role.valueOf(resultSet.getString("role"))
                    );
            } else {
                return null; // Optional
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
