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
import com.epam.clientinterface.controller.CustomExceptionHandler;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    private MockMvc mockMvc;
    private final String requestBody = String.format("{\"plan\":\"%s\"}", "BASE");

    @Mock
    CardService cardService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CardController(cardService))
            .setControllerAdvice(CustomExceptionHandler.class)
            .build();
    }

    @Test
    void shouldReturnIsCreatedIfRequestIsValid() throws Exception {
        when(cardService.createCard(anyLong(), any(Card.Plan.class))).thenReturn(new Card());

        this.mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"type\":\"BASE\"}",
        "{}"
    })
    public void shouldReturnValidationErrorResponseIfRequestIsIncorrect(String requestBody) throws Exception {
        this.mockMvc.perform(post("/account/1/cards")
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
        this.mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsAccountNotFound() throws Exception {

        doThrow(new AccountNotFoundException(2L))
            .when(cardService)
            .createCard(anyLong(), any(Card.Plan.class));

        this.mockMvc.perform(post("/account/2/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        this.mockMvc.perform(post("/account/1/cards")
            .contentType(MediaType.TEXT_HTML)
            .content("")).andExpect(status().isUnsupportedMediaType());
    }
}
