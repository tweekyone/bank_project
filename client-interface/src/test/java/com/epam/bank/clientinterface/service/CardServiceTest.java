package com.epam.bank.clientinterface.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.service.CardService;
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
    public void setUp() {
        cardService = new CardService(cardRepository, accountRepository);
    }

    @Test
    public void shouldReturnNewCardIfAccountIsExist() {
        when(accountRepository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, "", true, Account.Plan.BASE,
                1000, new User(), new ArrayList<>())));
        when(cardRepository.save(any(Card.class))).thenReturn(new Card());
        Card card = cardService.createCard(1L, CardPlan.BASE);
        Assertions.assertEquals(Card.class, card.getClass());
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
            () -> cardService.createCard(2L, CardPlan.BASE));
    }

    @Test
    public void shouldReturnCardNumber() {
        Assertions.assertEquals(16, cardService.generate(16).length());
    }

    @Test
    public void shouldReturnPinCode() {
        Assertions.assertEquals(4, cardService.generate(4).length());
    }

}
