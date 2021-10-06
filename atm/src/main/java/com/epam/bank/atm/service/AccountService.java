package com.epam.bank.atm.service;

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

public class AccountService {

    // private final String HOST = "jdbc:postgresql://localhost:5432/mydb";
    // private final String USERNAME = "postgres";
    // private final String PASSWORD = "root";
    //
    // // private final static String url = "jdbc:sqlite:db.sqlite";
    //
    //
    // public long
    //
    // public int updateAccount(String newName, int id) {
    //
    //     String sql = "UPDATE products SET name = ? WHERE id = ? ;";
    //
    //     try (Connection connection = DriverManager.getConnection(HOST);
    //          PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //     ) {
    //         preparedStatement.setString(1, newName);
    //         preparedStatement.setInt(2, id);
    //         preparedStatement.execute();
    //         return preparedStatement.executeUpdate();
    //
    //     } catch (SQLException e) {
    //         throw new SqlMappingException(e);
    //     }
    // }
    //
    // public List<Account> selectProductsByPrice(int minPrice, int maxPrice) {
    //
    //     String sql = "SELECT id, name, category, quantity, isAvailable," +
    //         " price FROM products WHERE price BETWEEN ? AND ? ;";
    //
    //     try (Connection connection = DriverManager.getConnection(HOST);
    //          PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //
    //     ) {
    //         preparedStatement.setInt(1, minPrice);
    //         preparedStatement.setInt(2, maxPrice);
    //         ResultSet resultSet = preparedStatement.executeQuery();
    //
    //         List<Account> results = new LinkedList<>();
    //         while (resultSet.next()) {
    //             results.add(new Product(
    //                 resultSet.getInt("id"),
    //                 resultSet.getString("name"),
    //                 resultSet.getString("category"),
    //                 resultSet.getInt("quantity"),
    //                 resultSet.getBoolean("isAvailable"),
    //                 resultSet.getInt("price")
    //             ));
    //         }
    //         resultSet.close();
    //         return results;
    //     } catch (SQLException e) {
    //         throw new SqlMappingException(e);
    //     }
    // }
    //
    // public List<Account> selectAllProducts() {
    //
    //     String sql = "SELECT id, name, category, quantity, isAvailable," +
    //         " price FROM products ;";
    //     try (Connection connection = DriverManager.getConnection(HOST);
    //          Statement statement = connection.createStatement();
    //          ResultSet resultSet = statement.executeQuery(sql);
    //     ) {
    //         List<Account> results = new LinkedList<>();
    //         while (resultSet.next()) {
    //             results.add(new Product(
    //                 resultSet.getInt("id"),
    //                 resultSet.getString("name"),
    //                 resultSet.getString("category"),
    //                 resultSet.getInt("quantity"),
    //                 resultSet.getBoolean("isAvailable"),
    //                 resultSet.getInt("price")
    //             ));
    //         }
    //         return results;
    //     } catch (SQLException e) {
    //         throw new SqlMappingException(e);
    //     }
    // }
    //
    // public List<Account> getOrdersByDate() {
    //
    //     String sql = "SELECT id, customer_id, product_id, " +
    //         "product_name, quantity," +
    //         " order_price, delivery, status," +
    //         " datetime FROM orders ORDER BY datetime DESC ;";
    //
    //     try (
    //         Connection connection = DriverManager.getConnection(HOST);
    //         Statement statement = connection.createStatement();
    //         ResultSet resultSet = statement.executeQuery(sql);
    //     ) {
    //         List<Account> orders = new LinkedList<>();
    //         while (resultSet.next()) {
    //             orders.add(new Order(
    //                 resultSet.getInt("id"),
    //                 resultSet.getInt("customer_id"),
    //                 resultSet.getInt("product_id"),
    //                 resultSet.getString("product_name"),
    //                 resultSet.getInt("quantity"),
    //                 resultSet.getInt("order_price"),
    //                 resultSet.getString("delivery"),
    //                 resultSet.getBoolean("status"),
    //                 resultSet.getString("datetime")
    //             ));
    //         }
    //         return orders;
    //     } catch (SQLException e) {
    //         throw new SqlMappingException(e);
    //     }
    //
    // }

}
