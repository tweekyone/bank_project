package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.AbstractControllerTest;
import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.controller.dto.response.CardResponse;
import com.epam.bank.operatorinterface.controller.dto.response.UserResponse;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.entity.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import util.TestDataFactory;

@WebMvcTest(controllers = {AccountMapperImpl.class, UserMapperImpl.class, CardMapperImpl.class})
public class ResponseMapperTest extends AbstractControllerTest {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CardMapper cardMapper;

    @Test
    public void shouldMapAccountToAccountResponse() {
        var account = TestDataFactory.getAccountWithCard();
        var accountResponse = accountMapper.map(account);

        assertAccountMapping(accountResponse, account);
    }

    @Test
    public void shouldMapClosedAccountToAccountResponse() {
        var account = TestDataFactory.getClosedAccount();
        var accountResponse = accountMapper.map(account);

        assertAccountMapping(accountResponse, account);
    }

    @Test
    public void shouldMapCardToCardResponse() {
        var card = TestDataFactory.getCard();
        var cardResponse = cardMapper.map(card);

        assertCardMapping(cardResponse, card);
    }

    @Test
    public void shouldMapUserToUserResponse() {
        var user = TestDataFactory.getUserWithAccount();
        var userResponse = userMapper.map(user);

        assertUserMapping(userResponse, user);
    }

    private void assertAccountMapping(AccountResponse accountResponse, Account account) {
        var config = new RecursiveComparisonConfiguration();
        config.ignoreFields("userId", "plan", "cards");
        Assertions.assertThat(accountResponse).usingRecursiveComparison(config).isEqualTo(account);
        Assertions.assertThat(accountResponse.getUserId()).isEqualTo(account.getUser().getId());
        Assertions.assertThat(accountResponse.getPlan()).isEqualTo(account.getPlan().name());
        Assertions.assertThat(accountResponse.getCards()).isEqualTo(cardMapper.map(account.getCards()));
    }

    private void assertCardMapping(CardResponse cardResponse, Card card) {
        var config = new RecursiveComparisonConfiguration();
        config.ignoreFields("accountId", "plan");
        Assertions.assertThat(cardResponse).usingRecursiveComparison(config).isEqualTo(card);
        Assertions.assertThat(cardResponse.getAccountId()).isEqualTo(card.getAccount().getId());
        Assertions.assertThat(cardResponse.getPlan()).isEqualTo(card.getPlan().name());
    }

    private void assertUserMapping(UserResponse userResponse, User user) {
        var config = new RecursiveComparisonConfiguration();
        config.ignoreFields("accounts");
        Assertions.assertThat(userResponse).usingRecursiveComparison(config).isEqualTo(user);
        Assertions.assertThat(userResponse.getAccounts()).isEqualTo(accountMapper.map(user.getAccounts()));
    }
}
