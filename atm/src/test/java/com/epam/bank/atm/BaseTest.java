package com.epam.bank.atm;

import com.epam.bank.atm.di.DIContainer;
import org.junit.jupiter.api.BeforeEach;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseTest {
    @BeforeEach
    public void resetDB() throws SQLException {
        var connection = DIContainer.instance().getSingleton(Connection.class);
        var query = "truncate table \"user\" restart identity cascade;"
            + "truncate table account restart identity cascade;"
            + "truncate table card restart identity cascade;"
            + "truncate table role restart identity cascade;"
            + "truncate table transaction restart identity cascade;"
            + "truncate table user_role restart identity cascade;"
            + "insert into \"user\" (name, surname, email, password, phone_number) "
            + "values ('User', 'Userovich', 'user@mail.com', 'pass', '+79996669966');"
            + "insert into account (number, is_default, plan, amount, user_id)"
            + "values ('40702810123456789125', true, 'plan', 5678.58, '1');"
            + "insert into card (number, pin_code, plan, explication_date, account_id)"
            + "values ('4070281012345678', '1234', 'TESTPLAN', '2025-10-07T00:00:00+00:00', 1);";

        connection.prepareStatement(query).executeUpdate();
    }
}
