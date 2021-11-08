package com.epam.bank.clientinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.bank.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.bank.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.bank.clientinterface.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class TransferControllerExternalTest {
    private MockMvc mockMvc;

    @Mock
    private AccountService accountServiceMock;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new TransferController(this.accountServiceMock))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    public void shouldReturnNoContentIfIncomeDataIsValid() throws Exception {
        var requestBody = this.getRequestBody(1L, "123", 1000.00);

        this.send(requestBody).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnValidationErrorResponseIfRequestIsIncorrect() throws Exception {
        this.send("{}")
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type", is("validation")))
            .andExpect(jsonPath("$.status", is(422)))
            .andExpect(jsonPath(
                "$.errors[*].field",
                containsInAnyOrder("sourceAccountId", "destinationAccountNumber", "amount")
            ))
            .andExpect(jsonPath(
                "$.errors[?(@.field=='sourceAccountId')].error",
                containsInAnyOrder("must be greater than 0")
            ))
            .andExpect(jsonPath(
                "$.errors[?(@.field=='destinationAccountNumber')].error",
                containsInAnyOrder("must not be blank")
            ))
            .andExpect(jsonPath(
                "$.errors[?(@.field=='amount')].error",
                containsInAnyOrder("must be greater than 0")
            ));
    }

    @Test
    public void shouldReturnEmptyBadRequestIfRequestIsInvalid() throws Exception {
        this.send("{invalid").andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        this.send("", MediaType.TEXT_HTML).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsAccountNotFound() throws Exception {
        var requestBody = this.getRequestBody(1L, "123", 1000.00);

        doThrow(new AccountNotFoundException(1L))
            .when(this.accountServiceMock)
            .externalTransfer(anyLong(), anyString(), anyDouble());

        this.send(requestBody)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        var requestBody = this.getRequestBody(1L, "123", 10000.00);

        doThrow(new NotEnoughMoneyException(1L, 10000.00))
            .when(this.accountServiceMock)
            .externalTransfer(anyLong(), anyString(), anyDouble());

        this.send(requestBody)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is("notEnoughMoney")))
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private String getRequestBody(long sourceAccountId, String destinationAccountNumber, double amount) {
        return String.format(
            "{\"sourceAccountId\":%d,\"destinationAccountNumber\":%s,\"amount\":%f}",
            sourceAccountId,
            destinationAccountNumber,
            amount
        );
    }

    private ResultActions send(String requestBody) throws Exception {
        return this.send(requestBody, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(String requestBody, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(post("/transfer/external").contentType(mediaType).content(requestBody));
    }
}
