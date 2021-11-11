package com.epam.bank.clientinterface.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.CardController;
import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CardController(cardService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldReturnIsOkIfCardBlocked() throws Exception {
        when(cardService.blockCard(anyLong())).thenReturn(new Card());

        this.mockMvc.perform(post("/card/6320/block"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsCardNotFound() throws Exception {
        doThrow(new CardNotFoundException(11L))
            .when(cardService)
            .blockCard(anyLong());

        mockMvc.perform(post("/card/11/block"))
            .andExpect(status().isNotFound());
    }

}
