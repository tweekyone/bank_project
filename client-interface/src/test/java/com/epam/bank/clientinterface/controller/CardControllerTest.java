package com.epam.bank.clientinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.CardController;
import com.epam.clientinterface.controller.CustomExceptionHandler;
import com.epam.clientinterface.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CardController(this.cardService))
            .setControllerAdvice(CustomExceptionHandler.class)
            .build();
    }

    @Test
    void shouldCreateNewCard() throws Exception {
        Long accountId = 1L;
        var requestBody = String.format("{\"plan\":\"%s\"}", "BASE");

       this.mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isCreated());
    }

}
