package util;

import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.controller.dto.response.UserResponse;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.AccountPlan;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.entity.CardPlan;
import com.epam.bank.operatorinterface.entity.User;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class TestDataFactory {
    public static User getUser() {
        var user = new User();
        user.setId(RandomUtils.nextLong());
        user.setName(RandomStringUtils.randomAlphabetic(8));
        user.setSurname(RandomStringUtils.randomAlphabetic(8));
        user.setPhoneNumber(RandomStringUtils.randomAlphabetic(8));
        user.setUsername(RandomStringUtils.randomAlphabetic(8));
        user.setEmail(RandomStringUtils.randomAlphabetic(8));
        user.setPassword(RandomStringUtils.randomAlphabetic(8));
        user.setEnabled(true);
        user.setFailedLoginAttempts(0);

        return user;
    }

    public static User getUserWithAccount() {
        return getAccount().getUser();
    }

    public static User getUserWithSeveralAccounts() {
        return getUserWithSeveralAccounts(2);
    }

    public static User getUserWithSeveralAccounts(int count) {
        var user = getUserWithAccount();
        var accounts = new ArrayList<>(user.getAccounts());
        for (var i = 0; i < count - 1; i++) {
            accounts.add(getNotDefaultAccount());
        }
        user.setAccounts(accounts);

        return user;
    }

    public static Account getAccount() {
        var account = new Account();
        account.setId(RandomUtils.nextLong());
        account.setNumber(RandomStringUtils.randomNumeric(20));
        account.setDefault(true);
        account.setPlan(getAccountPlan());
        account.setAmount(RandomUtils.nextDouble());

        var user = getUser();
        user.setAccounts(new ArrayList<>(List.of(account)));
        account.setUser(user);

        return account;
    }

    public static Account getNotDefaultAccount() {
        var account = getAccount();
        account.makeNotDefault();

        return account;
    }

    public static Card getCard() {
        var card = new Card();
        card.setId(RandomUtils.nextLong());
        card.setNumber(RandomStringUtils.randomNumeric(16));
        card.setPinCode(RandomStringUtils.randomNumeric(4));
        card.setPlan(getCardPlan());
        card.setBlocked(false);
        card.setExpirationDate(ZonedDateTime.now().plusYears(3));
        card.setPinCounter(0);

        var account = getAccount();
        account.setCards(new ArrayList<>(List.of(card)));
        card.setAccount(account);

        return card;
    }

    public static Card getCardWithMaxPinCounter() {
        var card = getCard();
        card.setPinCounter(3);

        return card;
    }

    public static Card getBlockedCard() {
        var card = getCard();
        card.setBlocked(true);

        return card;
    }

    public static Account getAccountWithCard() {
        return getCard().getAccount();
    }

    public static Account getClosedAccount() {
        var account = getAccountWithCard();
        account.close();

        return account;
    }

    public static AccountResponse getAccountResponse() {
        return new AccountResponse(
            RandomUtils.nextLong(),
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            getAccountPlan().name(),
            RandomUtils.nextDouble(),
            RandomUtils.nextLong(),
            List.of(),
            null
        );
    }

    public static UserResponse getUserResponse() {
        return new UserResponse(
            RandomUtils.nextLong(),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomNumeric(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            List.of(),
            true,
            0
        );
    }

    public static UserResponse getUserWithAccountResponse() {
        return new UserResponse(
            RandomUtils.nextLong(),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomNumeric(6),
            RandomStringUtils.randomAlphabetic(6),
            RandomStringUtils.randomAlphabetic(6),
            List.of(getAccountResponse()),
            true,
            0
        );
    }

    public static AccountPlan getAccountPlan() {
        return AccountPlan.values()[RandomUtils.nextInt(0, AccountPlan.values().length)];
    }

    public static CardPlan getCardPlan() {
        return CardPlan.values()[RandomUtils.nextInt(0, CardPlan.values().length)];
    }
}
