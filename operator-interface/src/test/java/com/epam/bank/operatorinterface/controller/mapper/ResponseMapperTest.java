package com.epam.bank.operatorinterface.controller.mapper;

import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.controller.dto.response.CardResponse;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Card;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import util.TestDataFactory;

public class ResponseMapperTest {
    private final ResponseMapper responseMapper = Mappers.getMapper(ResponseMapper.class);

    @Test
    public void shouldMapAccountToAccountResponse() {
        var account = TestDataFactory.getAccountWithCard();
        var accountResponse = responseMapper.map(account);

        assertAccountMapping(accountResponse, account);
    }

    @Test
    public void shouldMapClosedAccountToAccountResponse() {
        var account = TestDataFactory.getClosedAccount();
        var accountResponse = responseMapper.map(account);

        assertAccountMapping(accountResponse, account);
    }

    @Test
    public void shouldMapCardToCardResponse() {
        var card = TestDataFactory.getCard();
        var cardResponse = responseMapper.map(card);

        assertCardMapping(cardResponse, card);
    }

    private void assertAccountMapping(AccountResponse accountResponse, Account account) {
        var config = new RecursiveComparisonConfiguration();
        config.ignoreFields("userId", "plan", "cards");
        Assertions.assertThat(accountResponse).usingRecursiveComparison(config).isEqualTo(account);
        Assertions.assertThat(accountResponse.getUserId()).isEqualTo(account.getUser().getId());
        Assertions.assertThat(accountResponse.getPlan()).isEqualTo(account.getPlan().name());
        Assertions.assertThat(accountResponse.getCards()).isEqualTo(responseMapper.map(account.getCards()));
    }

    private void assertCardMapping(CardResponse cardResponse, Card card) {
        var config = new RecursiveComparisonConfiguration();
        config.ignoreFields("accountId", "plan");
        Assertions.assertThat(cardResponse).usingRecursiveComparison(config).isEqualTo(card);
        Assertions.assertThat(cardResponse.getAccountId()).isEqualTo(card.getAccount().getId());
        Assertions.assertThat(cardResponse.getPlan()).isEqualTo(card.getPlan().name());
    }
}
