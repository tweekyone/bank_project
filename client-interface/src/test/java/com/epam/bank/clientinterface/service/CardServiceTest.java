package com.epam.bank.clientinterface.service;

import static org.mockito.Mockito.when;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.AccountNotFoundException;
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
    public void beforeEach() {
        this.cardService = new CardService(cardRepository, accountRepository);
    }

    @Test
    public void shouldReturnNewCardIfAccountIsExist() {
        when(this.accountRepository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, "", true, Account.Plan.BASE,
                1000, new User(), new ArrayList<>())));
        this.cardService.releaseCard(1L, Card.Plan.BASE);
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotExist() {
        when(this.accountRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
            () -> this.cardService.releaseCard(2L, Card.Plan.BASE));
    }

}
