package com.epam.clientinterface.controller;

import static com.epam.clientinterface.controller.util.CardTestData.getTestCard;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.service.CardService;
import java.time.ZonedDateTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CardControllerBlockCardTest {

    private MockMvc mockMvc;

    @Mock
    CardService cardService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CardController(cardService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    public void shouldReturnIsOkIfRequestIsValid() throws Exception {
        when(cardService.blockCard(anyLong())).thenReturn(getTestCard(1));

        mockMvc.perform(patch("/card/1/blockCard"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsCardNotFound() throws Exception {
        doThrow(CardNotFoundException.class)
            .when(cardService)
            .blockCard(anyLong());

        mockMvc.perform(patch("/card/11/blockCard"))
            .andExpect(status().isNotFound());
    }

}
