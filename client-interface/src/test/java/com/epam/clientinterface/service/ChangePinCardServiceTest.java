package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.PinCounter;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.PinCounterRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangePinCardServiceTest {

    private CardService cardService;
    private ChangePinRequest changePinRequest;

    @Mock
    private CardRepository cardRepositoryMock;

    @Mock
    private PinCounterRepository pinCounterRepositoryMock;

    @BeforeEach
    public void setUp() {
        cardService = new CardService(cardRepositoryMock, pinCounterRepositoryMock);
        changePinRequest = new ChangePinRequest(1l, "1111", "1234");
    }

    @Test
    public void throwsCardNotFoundException() {
        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        CardNotFoundException thrownException = Assertions.assertThrows(CardNotFoundException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals(thrownException.getMessage(),
            String.format("Card with id %s not found", changePinRequest.getCardId()));
    }

    @Test
    public void throwsChangePinException() {
        Account account = new Account(1l, "", true, Account.Plan.TESTPLAN,
            1000, new User(), new ArrayList<>());
        Card card = new Card(1l, "", account, "1111", Card.Plan.TESTPLAN,
            LocalDateTime.now(), null);
        PinCounter pinCounter = new PinCounter(1l, card, LocalDateTime.now(), 3);

        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(pinCounterRepositoryMock.findByCardId(Mockito.anyLong())).thenReturn(pinCounter);

        ChangePinException thrownException = Assertions.assertThrows(ChangePinException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals(thrownException.getMessage(),
            "You have already changed your pin for 3 times! Try again tomorrow!");
    }
}