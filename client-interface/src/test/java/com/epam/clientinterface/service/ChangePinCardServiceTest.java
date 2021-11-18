package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.CardRepository;
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

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepositoryMock;

    @BeforeEach
    public void setUp() {
        changePinRequest = new ChangePinRequest(1L, "1111", "1234");
        testAccount = new Account(1L, "", true, Account.Plan.BASE,
            1000, new User(), new ArrayList<>(), LocalDateTime.now());
    }

    @Test
    public void throwsCardNotFoundException() {
        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        CardNotFoundException thrownException = Assertions.assertThrows(CardNotFoundException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals(String.format("Card with id %s not found", changePinRequest.getCardId()),
            thrownException.getMessage());
    }

    @Test
    public void throwsChangePinException() {
        Card testCard = new Card(testAccount, "1234567887654321", "1111",
            CardPlan.BASE, false, LocalDateTime.now(), 3);

        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testCard));

        ChangePinException thrownException = Assertions.assertThrows(ChangePinException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals("You have already changed your pin for 3 times! Try again tomorrow!",
            thrownException.getMessage());
    }

    @Test
    public void changePinIfHaveAttempts() {
        Card testCard = new Card(testAccount, "1234567887654321", "1111",
            CardPlan.BASE, false, LocalDateTime.now(), 2);
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);

        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(testCard));

        cardService.changePinCode(changePinRequest);

        Mockito.verify(cardRepositoryMock).save(cardArgumentCaptor.capture());

        Assertions.assertEquals(3, cardArgumentCaptor.getValue().getPinCounter());
    }
}