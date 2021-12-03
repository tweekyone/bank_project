package com.epam.clientinterface.controller.util;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import java.time.ZonedDateTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class CardTestData {

    public static Card getTestCard(long id) {
        return getTestCard(id, new Account());
    }

    public static Card getTestCard(long id, Account account) {
        return new Card(
            id,
            RandomStringUtils.randomNumeric(20),
            account,
            RandomStringUtils.randomNumeric(4),
            CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)],
            false,
            ZonedDateTime.now(),
            0
        );
    }
}
