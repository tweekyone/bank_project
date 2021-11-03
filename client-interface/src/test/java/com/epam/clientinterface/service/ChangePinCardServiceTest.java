package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.CardController;
import com.epam.clientinterface.controller.advice.CustomErrorHandler;
import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.PinCounterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChangePinCardServiceTest {

    private CardService cardService;
    private ChangePinRequest changePinRequest;

    @Mock
    private CardRepository cardRepositoryMock;

    @Mock
    private PinCounterRepository pinCounterRepositoryMock;

    //@BeforeAll
    @BeforeEach
    public void setUp(){
        cardService = new CardService(cardRepositoryMock, pinCounterRepositoryMock);
        changePinRequest = new ChangePinRequest(1l, "1111", "1234");
    }

    @Test
    public void throwsCardNotFoundException(){
        Mockito.when(cardRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        CardNotFoundException thrownException = Assertions.assertThrows(CardNotFoundException.class,
            () -> cardService.changePinCode(changePinRequest));

        Assertions.assertEquals(thrownException.getMessage(),
            String.format("Card with id %s not found", changePinRequest.getCardId()));
    }

}