package com.epam.bank.operatorinterface.controller;

import static com.epam.bank.operatorinterface.util.JsonHelper.fromJson;
import static com.epam.bank.operatorinterface.util.JsonHelper.toJson;
import static com.epam.bank.operatorinterface.util.TestDataFactory.getCardRequest;
import static com.epam.bank.operatorinterface.util.TestDataFactory.getCardResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.domain.dto.CardRequest;
import com.epam.bank.operatorinterface.domain.dto.CardResponse;
import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.enumerated.CardPlan;
import com.epam.bank.operatorinterface.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CardControllerTestCreatingBlockingClosing {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    CardService cardService;

    @Autowired
    public CardControllerTestCreatingBlockingClosing(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void createCardSuccess() throws Exception {
        CardRequest request = new CardRequest(1, CardPlan.BASE);
        doReturn(getCardResponse(1)).when(cardService).createCard(request);
        MvcResult createResult = mockMvc
            .perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
            .andExpect(status().isCreated())
            .andReturn();

        CardResponse response =
            fromJson(objectMapper, createResult.getResponse().getContentAsString(), CardResponse.class);
        assertNotNull(response.getId(), "Card id must mot be null");
        assertEquals(16, response.getCardNumber().length());
        LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(response.getCardNumber());
        assertTrue(response.getExpirationDate().isAfter(LocalDate.now()));
    }

    @Test
    void blockCardSuccess() throws Exception {
        mockMvc.perform(put("/cards/1/block"))
            .andExpect(status().isNoContent());
    }

    @Test
    void closeCardSuccess() throws Exception {
        mockMvc.perform(put("/cards/1/close"))
            .andExpect(status().isNoContent());
    }

    @Test //"{\"accountId\":\"89\",\"plan\": \"BASE\"}\"")
    void shouldReturnIsNotFound_IfAccountDoesNotExist() throws Exception {
        CardRequest request = getCardRequest(5454645);
        doThrow(new NotFoundException(Account.class, request.getAccountId()))
            .when(cardService).createCard(any(CardRequest.class));
        mockMvc.perform(
                post("/cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(objectMapper, request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_IfAccountNumberIsIncorrect() throws Exception {
        CardRequest request = getCardRequest(-5454645);
        mockMvc.perform(
                post("/cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(objectMapper, request)))
            .andExpect(status().isUnprocessableEntity());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"plan\":\"ghghghgh\"}", "{\"plan\":\"BASE\"", ""})
    void shouldReturnBadRequest_IfCardPlanOrWholeRequestAreIncorrect(String requestBody) throws Exception {
        mockMvc.perform(
                post("/cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}"})
    void shouldReturnUnprocessableEntity_IfCardPlanIsNotProvided(String requestBody) throws Exception {
        mockMvc.perform(
                post("/cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturnIsNotFound_IfCardDoesNotExist() throws Exception {
        doThrow(new NotFoundException(Card.class, 6565656))
            .when(cardService).blockCard(6565656);
        mockMvc.perform(put("/cards/6565656/block")).andExpect(status().isNotFound());

        doThrow(new NotFoundException(Card.class, 6565656))
            .when(cardService).closeCard(6565656);
        mockMvc.perform(put("/cards/6565656/close")).andExpect(status().isNotFound());
    }
}
