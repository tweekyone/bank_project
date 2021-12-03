package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.entity.TransactionAccountData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcTransactionRepository implements TransactionRepository {
    private final Connection connection;

    public JdbcTransactionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Transaction transaction) {
        var query = "insert into transaction "
            + "("
            + "source_account_number, "
            + "source_is_external, "
            + "destination_account_number, "
            + "destination_is_external, "
            + "amount, "
            + "date_time, "
            + "operation_type, "
            + "state"
            + ") "
            + "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (var statement = this.connection.prepareStatement(query)) {
            if (transaction.getSourceAccount() == null) {
                statement.setNull(1, Types.VARCHAR);
                statement.setNull(2, Types.BOOLEAN);
            } else {
                statement.setString(1, transaction.getSourceAccount().getAccountNumber());
                statement.setBoolean(2, transaction.getSourceAccount().isExternal());
            }

            if (transaction.getDestinationAccount() == null) {
                statement.setNull(3, Types.VARCHAR);
                statement.setNull(4, Types.BOOLEAN);
            } else {
                statement.setString(3, transaction.getDestinationAccount().getAccountNumber());
                statement.setBoolean(4, transaction.getDestinationAccount().isExternal());
            }

            statement.setDouble(5, transaction.getAmount());
            statement.setTimestamp(6, Timestamp.valueOf(transaction.getDateTime()));
            statement.setString(7, transaction.getOperationType().toString());
            statement.setString(8, transaction.getState().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> getById(long id) {
        var query = "select * from transaction where id = ?";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            return resultSet.next() ? Optional.of(this.mapRecordToTransaction(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getByAccountNumber(String accountNumber) {
        var query = "select * from transaction where source_account_number = ? or destination_account_number = ?";
        try (var statement = this.connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountNumber);

            var resultSet = statement.executeQuery();

            var transactionList = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactionList.add(this.mapRecordToTransaction(resultSet));
            }

            return transactionList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction mapRecordToTransaction(ResultSet resultSet) throws SQLException {
        var sourceAccountNumber = resultSet.getObject("source_account_number", String.class);
        var sourceIsExternal = resultSet.getObject("source_is_external", Boolean.class);
        var destinationAccountNumber = resultSet.getObject("destination_account_number", String.class);
        var destinationIsExternal = resultSet.getObject("destination_is_external", Boolean.class);

        var sourceAccount = sourceAccountNumber != null && sourceIsExternal != null
            ? new TransactionAccountData(sourceAccountNumber, sourceIsExternal)
            : null;

        var destinationAccount = destinationAccountNumber != null && destinationIsExternal != null
            ? new TransactionAccountData(destinationAccountNumber, destinationIsExternal)
            : null;

        return new Transaction(
            resultSet.getLong("id"),
            sourceAccount,
            destinationAccount,
            resultSet.getDouble("amount"),
            resultSet.getTimestamp("date_time").toLocalDateTime(),
            Transaction.OperationType.valueOf(resultSet.getString("operation_type")),
            Transaction.State.valueOf(resultSet.getString("state"))
        );
    }
}
