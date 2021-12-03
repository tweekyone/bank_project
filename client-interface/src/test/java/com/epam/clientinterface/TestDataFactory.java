package com.epam.clientinterface;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestDataFactory {
    public static Account getAccount() {
        var account = new Account(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            getUser(),
            new ArrayList<>(),
            null
        );
        var user = getUser(account);
        account.setUser(user);

        return account;
    }

    public static Account getClosedAccount() {
        var account = new Account(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            new User(),
            new ArrayList<>(),
            LocalDateTime.now()
        );
        var user = getUser(account);
        account.setUser(user);

        return account;
    }

    public static Card getCard() {
        return new Card(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(16),
            getAccount(),
            RandomStringUtils.randomNumeric(4),
            CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)],
            false,
            LocalDateTime.now().plusYears(3),
            0
        );
    }

    public static Card getCardWithClosedAccount() {
        return new Card(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(16),
            getClosedAccount(),
            RandomStringUtils.randomNumeric(4),
            CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)],
            false,
            LocalDateTime.now().plusYears(3),
            0
        );
    }

    public static User getUser() {
        return new User(
            RandomUtils.nextLong(),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomNumeric(7),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphanumeric(4),
            new ArrayList<>(),
            true,
            0,
            new HashSet<>()
        );
    }

    public static User getUser(Account account) {
        return new User(
            RandomUtils.nextLong(),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomNumeric(7),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphabetic(4),
            RandomStringUtils.randomAlphanumeric(4),
            List.of(account),
            true,
            0,
            new HashSet<>()
        );
    }
}
