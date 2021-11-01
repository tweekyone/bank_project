package com.epam.bank.clientinterface.service;

import static org.mockito.Mockito.when;

import com.epam.clientinterface.controller.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.controller.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.service.CardService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    public void beforeEach() {
        cardService = new CardService(cardRepository, accountRepository);
    }

    @Test
    public void shouldReturnNewCardIfAccountIsExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account()));
        cardService.releaseCard(1L, CardPlan.BASE);
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountNotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
            () -> cardService.releaseCard(2L, CardPlan.BASE));
    }

    @Test
    public void shouldBlockCardIfCardIsExist() {

        Account account = new Account(1L, "", true, Account.Plan.BASE,
            1000, new User(), new ArrayList<>());
        Card card = new Card(account, "1234567887654321","1111",
            CardPlan.BASE, false, LocalDateTime.now());
        Card saveCard = new Card(account, "1234567887654321","1111",
            CardPlan.BASE, true, LocalDateTime.now());;

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(saveCard);
        Card returnCard = cardService.blockCard(1L);
        Assertions.assertEquals(returnCard, saveCard);
        Assertions.assertTrue(card.isBlocked());
    }

    @Test
    public void shouldThrowCardNotFoundIfCardNotFound() {
        when(this.cardRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(CardNotFoundException.class,
            () -> this.cardService.blockCard(1L));
    }

}
