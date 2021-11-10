package com.epam.bank.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangePinCardServiceTest {
    private ChangePinRequest changePinRequest;
    private Account testAccount;
    private Card testCard;

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @BeforeEach
    public void setUp() {
        changePinRequest = new ChangePinRequest(1L, "1111", "1234");
        testAccount = new Account(1L, "", true, Account.Plan.BASE,
            1000, new User(), new ArrayList<>());
        testCard = new Card(1L, "", testAccount, "1111", CardPlan.BASE,
            LocalDateTime.now(), 0);
    }

    @Test
    public void throwsCardNotFoundException() {
        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        CardNotFoundException thrownException = Assertions.assertThrows(CardNotFoundException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals(String.format("Card with id %s not found", changePinRequest.getCardId()),
            thrownException.getMessage());
    }
    //
    // @Test
    // public void throwsChangePinException() {
    //     PinCounter pinCounter = new PinCounter(1L, testCard, LocalDateTime.now(), 3);
    //
    //     Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testCard));
    //     Mockito.when(pinCounterRepositoryMock.findByCardId(Mockito.anyLong())).thenReturn(Optional.of(pinCounter));
    //
    //     ChangePinException thrownException = Assertions.assertThrows(ChangePinException.class,
    //         () -> cardService.changePinCode(changePinRequest));
    //
    //     Assertions.assertEquals("You have already changed your pin for 3 times! Try again tomorrow!",
    //         thrownException.getMessage());
    // }
    //
    // @Test
    // public void changePinIfLastChangeToday() {
    //     PinCounter pinCounter = new PinCounter(1L, testCard, LocalDateTime.now(), 2);
    //     ArgumentCaptor<PinCounter> pinCounterArgumentCaptor = ArgumentCaptor.forClass(PinCounter.class);
    //     ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
    //
    //     Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testCard));
    //     Mockito.when(pinCounterRepositoryMock.findByCardId(Mockito.anyLong())).thenReturn(Optional.of(pinCounter));
    //
    //     cardService.changePinCode(changePinRequest);
    //
    //     Mockito.verify(pinCounterRepositoryMock).save(pinCounterArgumentCaptor.capture());
    //     Mockito.verify(cardRepositoryMock).save(cardArgumentCaptor.capture());
    //
    //     Assertions.assertEquals(pinCounter, pinCounterArgumentCaptor.getValue());
    //     Assertions.assertEquals(testCard, cardArgumentCaptor.getValue());
    // }
    //
    // @Test
    // public void changePinIfLastChangeNotToday() {
    //     PinCounter pinCounter = new PinCounter(1L, testCard, LocalDateTime.now().minusDays(1), 3);
    //     ArgumentCaptor<PinCounter> pinCounterArgumentCaptor = ArgumentCaptor.forClass(PinCounter.class);
    //     ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
    //
    //     Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testCard));
    //     Mockito.when(pinCounterRepositoryMock.findByCardId(Mockito.anyLong())).thenReturn(Optional.of(pinCounter));
    //
    //     cardService.changePinCode(changePinRequest);
    //
    //     Mockito.verify(pinCounterRepositoryMock).save(pinCounterArgumentCaptor.capture());
    //     Mockito.verify(cardRepositoryMock).save(cardArgumentCaptor.capture());
    //
    //     Assertions.assertEquals(pinCounter, pinCounterArgumentCaptor.getValue());
    //     Assertions.assertEquals(testCard, cardArgumentCaptor.getValue());
    // }
}