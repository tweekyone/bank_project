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

import com.epam.bank.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.bank.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.bank.clientinterface.entity.Card;
import com.epam.bank.clientinterface.entity.CardPlan;
import com.epam.bank.clientinterface.service.CardService;
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
public class CardControllerNewCardTest {

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
        when(cardService.releaseCard(anyLong(), any(CardPlan.class))).thenReturn(new Card());

        mockMvc.perform(post("/account/1/releaseCard")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"plan\":null}",
        "{\"type\":\"BASE\"}",
        "{}"
    })
    public void shouldReturnValidationErrorResponseIfRequestIsIncorrect(String requestBody) throws Exception {
        mockMvc.perform(post("/account/1/releaseCard")
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
        mockMvc.perform(post("/account/1/releaseCard")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsAccountNotFound() throws Exception {
        doThrow(AccountNotFoundException.class)
            .when(cardService)
            .releaseCard(anyLong(), any(CardPlan.class));

        mockMvc.perform(post("/account/2/releaseCard")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        mockMvc.perform(post("/account/1/releaseCard")
            .contentType(MediaType.TEXT_HTML)
            .content("")).andExpect(status().isUnsupportedMediaType());
    }
}
