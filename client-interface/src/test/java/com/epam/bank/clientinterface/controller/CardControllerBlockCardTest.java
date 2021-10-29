package com.epam.bank.clientinterface.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.CardController;
import com.epam.clientinterface.controller.CustomExceptionHandler;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.CardNotFoundException;
import com.epam.clientinterface.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            .setControllerAdvice(CustomExceptionHandler.class)
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

        this.mockMvc.perform(post("/card/11/block"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("cardNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

}
