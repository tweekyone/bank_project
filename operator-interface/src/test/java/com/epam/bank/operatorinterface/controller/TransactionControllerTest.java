package com.epam.bank.operatorinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest extends AbstractControllerTest {

    private final String url = "/transactions/{transactionId}";
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    TransactionService transactionService;

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
