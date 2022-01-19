package com.epam.bank.operatorinterface.util;

import static com.epam.bank.operatorinterface.service.CardUtil.generateCardNumber;
import static com.epam.bank.operatorinterface.service.CardUtil.generatePinCode;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import com.epam.bank.operatorinterface.domain.dto.CardDto;
import com.epam.bank.operatorinterface.domain.dto.CardRequest;
import com.epam.bank.operatorinterface.domain.dto.CardResponse;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.AccountPlan;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.entity.CardPlan;
import com.epam.bank.operatorinterface.entity.Role;
import com.epam.bank.operatorinterface.entity.User;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;

public class TestDataFactory {

    public static CardDto getCardDto(long id) {
        return new CardDto(id, randomNumeric(16), CardPlan.BASE, false, ZonedDateTime.now().plusYears(3));
    }

    public static Card getCard(long id) {
        return new Card(id, generateCardNumber(), generatePinCode(),
            CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)],
            false, ZonedDateTime.now().plusYears(3), 0, false, new Account());
    }

    public static CardResponse getCardResponse(long id) {
        return new CardResponse(id, randomNumeric(16), ZonedDateTime.now().plusYears(3).toLocalDate());
    }

    public static CardRequest getCardRequest(long id) {
        return new CardRequest(id, CardPlan.BASE);
    }

    public static Account getAccount(long id) {
        return new Account(id, randomNumeric(20), true, AccountPlan.BASE, 100.12, new User(), List.of(getCard(152)),
            LocalDateTime.of(2025, 12, 12, 0, 0, 1));
    }

    public static Account getClosedAccount(long id) {
        return new Account(id, randomNumeric(20), true, AccountPlan.BASE, 100.12, new User(), List.of(getCard(152)),
            LocalDateTime.now().minusDays(1));
    }

    public static Account getAccountWithUser(long id) {
        return new Account(id, randomNumeric(20), true, AccountPlan.BASE, 100.12, getUser(), List.of(getCard(152)),
            LocalDateTime.of(2025, 12, 12, 0, 0, 1));
    }

    public static User getUser() {
        return new User(1L, "User", "Userovich",
            "+8979878512121", "username",  "aa@email.com",
            "$2a$10$g2SYZuOzPlc5l9a9FPF7ReW09tmapH2VI86W/uv2V/eICElXqxm6u", true, 0,
            List.of(),
            new HashSet<>(List.of(new Role(1L, "USER"))));
    }

}
