package com.epam.bank.operatorinterface.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.service.TransactionService;
import com.epam.bank.operatorinterface.service.importpkg.TransactionImportType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private final String url = "/transactions/";
    private final String importUrl = url + "import";
    private final String transactionIdParam = "{transactionId}";

    private final MockMultipartFile fileMock = new MockMultipartFile("file",
        new ByteArrayInputStream(RandomStringUtils.randomAlphabetic(100)
            .getBytes(StandardCharsets.UTF_8)));

    private MockMvc mockMvc;

    @Mock
    TransactionService transactionService;

    ObjectMapper mapper = new ObjectMapper();

    public TransactionControllerTest() throws IOException {
    }

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

        mockMvc.perform(MockMvcRequestBuilders.get(url + transactionIdParam, transactionId))
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

        mockMvc.perform(get(url + transactionIdParam, randomId))
            .andExpect(status().isNotFound());

        Mockito.verify(transactionService).getTransactionById(ArgumentMatchers.anyLong());
    }

    @Test
    void shouldImportCsvFile() throws Exception {
        mockMvc.perform(multipart(importUrl)
            .file(fileMock)
            .param("type", TransactionImportType.CSV.name())
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isNoContent());

        Mockito.verify(transactionService).importTransactions(any(), any());
    }

    @Test
    void shouldThrowMethodArgumentTypeMismatchException_ThenImportingWrongFileType() throws Exception {
        mockMvc.perform(multipart(importUrl)
            .file(fileMock)
            .param("type", RandomStringUtils.randomAlphabetic(20))
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(
                result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }


    @Test
    void shouldThrowMissingServletRequestParameterException_ThenImportingCsvWithMissingParams() throws Exception {
        mockMvc.perform(multipart(importUrl)
            .file(fileMock)
            .param(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(20))
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(
                result -> assertTrue(result.getResolvedException() instanceof MissingServletRequestParameterException));
    }

    @Test
    void shouldThrowRuntimeException_ThenImportingCsvWithIncorrectContent() throws Exception {
        Mockito.doThrow(RuntimeException.class)
            .when(transactionService)
            .importTransactions(any(), any());

        mockMvc.perform(multipart(importUrl)
            .file(fileMock)
            .param("type", TransactionImportType.CSV.name())
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));

        Mockito.verify(transactionService).importTransactions(any(), any());
    }
}
