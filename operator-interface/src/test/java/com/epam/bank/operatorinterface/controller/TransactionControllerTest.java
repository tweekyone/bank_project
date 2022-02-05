package com.epam.bank.operatorinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private final String url = "/transactions/{transactionId}";

    private MockMvc mockMvc;

    @Mock
    TransactionService transactionService;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new TransactionController(transactionService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldReturnTransactionById() throws Exception {
        final var transactionId = RandomUtils.nextLong();
        final var expectedTransaction = new Transaction();

        Mockito.when(transactionService.getTransactionById(transactionId))
            .thenReturn(expectedTransaction);

        mockMvc.perform(MockMvcRequestBuilders.get(url, transactionId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(expectedTransaction)));
        // .andDo(MockMvcResultHandlers.print());

        Mockito.verify(transactionService)
            .getTransactionById(ArgumentMatchers.anyLong());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsTransactionNotFoundException_WhenGetById() throws Exception {
        final var randomId = RandomUtils.nextLong();

        Mockito.doThrow(TransactionNotFoundException.class)
            .when(transactionService)
            .getTransactionById(randomId);

        mockMvc.perform(get(url, randomId))
            .andExpect(status().isNotFound());

        Mockito.verify(transactionService).getTransactionById(ArgumentMatchers.anyLong());
    }

}
