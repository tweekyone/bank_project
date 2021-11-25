package com.epam.clientinterface;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestDataFactory {
    public static Account getAccount() {
        return new Account(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            new User(),
            new ArrayList<>(),
            null
        );
    }

    public static Account getClosedAccount() {
        return new Account(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            new User(),
            new ArrayList<>(),
            LocalDateTime.now()
        );
    }

    public static Card getCard() {
        return new Card(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(16),
            getAccount(),
            RandomStringUtils.randomNumeric(4),
            CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)],
            false,
            LocalDateTime.now().plusYears(3)
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
            LocalDateTime.now().plusYears(3)
        );
    }
}
