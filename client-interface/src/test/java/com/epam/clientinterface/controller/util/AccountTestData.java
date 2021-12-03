package com.epam.clientinterface.controller.util;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class AccountTestData {

    public static Account getTestAccount(long id) {
        return new Account(
            id,
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            new User(),
            new ArrayList<>(),
            null
        );
    }
}
