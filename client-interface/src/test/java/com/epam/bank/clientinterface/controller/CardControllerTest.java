package com.epam.bank.clientinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.CardController;
import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.service.CardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    private MockMvc mockMvc;
    private final String requestBody = String.format("{\"plan\":\"%s\"}", "BASE");

    @Mock
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CardController(cardService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldReturnIsCreatedIfRequestIsValid() throws Exception {
        when(cardService.createCard(anyLong(), any(CardPlan.class))).thenReturn(new Card());

        mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"type\":\"BASE\"}",
        "{}"
    })
    public void shouldReturnValidationErrorResponseIfRequestIsIncorrect(String requestBody) throws Exception {
        mockMvc.perform(post("/account/1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("validation")))
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath(
                    "$.errors[?(@.field=='plan')].error",
                    containsInAnyOrder("must not be null")
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"plan\":\"BAS\"}",
        "{\"plan\":\"BASE\"",
        ""
    })
    public void shouldReturnBadRequestIfRequestIsInvalid(String requestBody) throws Exception {
        mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotFound() throws Exception {

        doThrow(new AccountNotFoundException(2L))
            .when(cardService)
            .createCard(anyLong(), any(CardPlan.class));

        Assertions.assertThrows(AccountNotFoundException.class,
            () -> cardService.createCard(2L, CardPlan.BASE));
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.TEXT_HTML)
            .content("")).andExpect(status().isUnsupportedMediaType());
    }
}
