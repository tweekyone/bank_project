package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.controller.dto.request.ReadTransactionsRequest;
import com.epam.clientinterface.controller.util.JsonHelper;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    private static final String READTRANSACTIONS = "/read-transactions";
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionServiceMock;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new TransactionController(transactionServiceMock))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldReturnOkStatus() throws Exception {
        ReadTransactionsRequest transactionsRequest = new ReadTransactionsRequest(
            1L, "40702810123456789125");

        mockMvc.perform(MockMvcRequestBuilders.post(READTRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(new ObjectMapper(), transactionsRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsAccountNotFoundException() throws Exception {
        ReadTransactionsRequest transactionsRequest = new ReadTransactionsRequest(
            1L, "123456");

        Mockito.doThrow(new AccountNotFoundException(1L))
            .when(transactionServiceMock)
            .readTransactions(Mockito.anyLong(), Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.post(READTRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(new ObjectMapper(), transactionsRequest)))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is("accountNotFound")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    void shouldReturnUnprocessableEntityStatusIfRequestIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(READTRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void shouldReturnBadRequestIfRequestIsIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(READTRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{incorrect}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}