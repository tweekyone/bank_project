package com.epam.bank.operatorinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.bank.operatorinterface.controller.mapper.AccountMapper;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.service.AccountService;
import com.epam.bank.operatorinterface.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
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
class AccountControllerTransactionsTest {

    private final String urlFull = "/accounts/{accountNumber}/transactions";
    private final String accountNumber = RandomStringUtils.randomNumeric(20);

    private final List<Transaction> transactions = getTransactionsMock();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String expectedJson = mapper.writeValueAsString(transactions);

    private MockMvc mockMvc;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private AccountMapper responseMapper;

    @Mock
    TransactionService transactionService;

    AccountControllerTransactionsTest() throws JsonProcessingException {
    }

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new AccountController(accountServiceMock, responseMapper, transactionService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldReturnAllTransactionsByAccountNumber() throws Exception {
        Mockito.when(transactionService.getAllTransactionsByAccountNumber(accountNumber))
            .thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get(urlFull, accountNumber))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(expectedJson));

        Mockito.verify(transactionService)
            .getAllTransactionsByAccountNumber(ArgumentMatchers.anyString());
    }

    @Test
    void shouldReturnTransactionsByDateTime() throws Exception {
        final var dateTime = ZonedDateTime.now().withFixedOffsetZone();

        Mockito.when(transactionService.getAccountTransactionsByDateTime(accountNumber, dateTime))
            .thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get(
            urlFull, accountNumber, dateTime)
            .param("dateTime", String.valueOf(dateTime))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(expectedJson));

        Mockito.verify(transactionService)
            .getAccountTransactionsByDateTime(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    void shouldReturnTransactionsByDate() throws Exception {
        final var date = LocalDate.now();

        Mockito.when(transactionService.getAccountTransactionsByDate(accountNumber, date))
            .thenReturn(transactions);

        mockMvc.perform(get(
            urlFull, accountNumber, date)
            .param("date", String.valueOf(date))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(expectedJson));

        Mockito.verify(transactionService)
            .getAccountTransactionsByDate(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    void shouldReturnTransactionsByYearMonth() throws Exception {
        final var yearMonth = YearMonth.now();

        Mockito.when(transactionService.getAccountTransactionsByYearMonth(accountNumber, yearMonth))
            .thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get(
            urlFull, accountNumber, yearMonth)
            .param("yearMonth", String.valueOf(yearMonth))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(expectedJson));

        Mockito.verify(transactionService)
            .getAccountTransactionsByYearMonth(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    private List<Transaction> getTransactionsMock() {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction());
        return transactionList;
    }

}
