package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.controller.util.JsonHelper;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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
    private static final String READTRANSACTIONS = "/transactions";
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
        String userId = RandomStringUtils.random(5, false, true);
        String accountNumber = RandomStringUtils.random(8, false, true);

        mockMvc.perform(MockMvcRequestBuilders.get(READTRANSACTIONS
                    + "/{userId}/{accountNumber}",userId, accountNumber))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsAccountNotFoundException() throws Exception {
        String userId = RandomStringUtils.random(5, false, true);
        String accountNumber = RandomStringUtils.random(8, false, true);

        Mockito.doThrow(new AccountNotFoundException(1L))
            .when(transactionServiceMock)
            .readTransactions(Mockito.anyLong(), Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.get(READTRANSACTIONS
                + "/{userId}/{accountNumber}",userId, accountNumber))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is("accountNotFound")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Is.is(HttpStatus.NOT_FOUND.value())));
    }
}