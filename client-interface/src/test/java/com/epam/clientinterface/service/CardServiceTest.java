package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.FixtureFactory;
import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void shouldReturnNewCardIfAccountIsExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(FixtureFactory.getAccount()));

        LocalDateTime dateTimeBefore = LocalDateTime.now().plusYears(3);
        cardService.releaseCard(1L, CardPlan.BASE);
        LocalDateTime dateTimeAfter = LocalDateTime.now().plusYears(3);

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card saveCard = cardCaptor.getValue();
        Assertions.assertTrue(
            dateTimeBefore.isBefore(saveCard.getExpirationDate())
                || dateTimeBefore.isEqual(saveCard.getExpirationDate())
        );
        Assertions.assertTrue(
            dateTimeAfter.isAfter(saveCard.getExpirationDate())
                || dateTimeAfter.isEqual(saveCard.getExpirationDate())
        );
        Assertions.assertEquals(saveCard.getPlan(), CardPlan.BASE);
        Assertions.assertEquals(saveCard.getNumber().length(), 16);
        Assertions.assertEquals(saveCard.getPinCode().length(), 4);
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotExist() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
            () -> cardService.releaseCard(2L, CardPlan.BASE));
    }

    @Test
    public void shouldReturnDifferentCards() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(FixtureFactory.getAccount()));
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);

        cardService.releaseCard(1L, CardPlan.BASE);
        cardService.releaseCard(1L, CardPlan.BASE);
        verify(cardRepository, times(2)).save(cardCaptor.capture());

        Card card1 = cardCaptor.getAllValues().get(0);
        Card card2 = cardCaptor.getAllValues().get(1);
        Assertions.assertNotEquals(card1.getNumber(), card2.getNumber());
    }

    @Test
    public void shouldThrowAccountIsClosedIfAccountIsClosedWhenReleaseNewCard() {
        when(this.accountRepository.findById(anyLong())).thenReturn(Optional.of(FixtureFactory.getClosedAccount()));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.cardService.releaseCard(RandomUtils.nextLong(), CardPlan.BASE)
        );
    }

    @Test
    public void shouldReturnCardNumber() {
        Assertions.assertEquals(16, cardService.randomGenerateStringOfInt(16).length());
    }

    @Test
    public void shouldReturnPinCode() {
        Assertions.assertEquals(4, cardService.randomGenerateStringOfInt(4).length());
    }

    @Test
    public void shouldBlockCardIfCardIsExist() {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(FixtureFactory.getCard()));

        cardService.blockCard(1L);

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card blockedCard = cardCaptor.getValue();
        verify(cardRepository).save(blockedCard);
        Assertions.assertTrue(blockedCard.isBlocked());
    }

    @Test
    public void shouldThrowCardNotFoundIfCardDoesNotExist() {
        when(this.cardRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(CardNotFoundException.class,
            () -> this.cardService.blockCard(1L));
    }

    @Test
    public void shouldThrowAccountIsClosedIfAccountIsClosedWhenBlockCard() {
        var cardFixture = FixtureFactory.getCardWithClosedAccount();

        when(this.cardRepository.findById(anyLong()))
            .thenReturn(Optional.of(cardFixture));

        Assertions.assertThrows(AccountIsClosedException.class, () -> this.cardService.blockCard(cardFixture.getId()));
    }
}
