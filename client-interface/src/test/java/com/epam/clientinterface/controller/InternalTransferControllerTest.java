package com.epam.clientinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.service.AccountService;
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
public class InternalTransferControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AccountService accountServiceMock;

    private final String requestBodyTmpl = "{\"sourceAccountId\":%d,\"destinationAccountId\":%d,\"amount\":%f}";

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new InternalTransferController(this.accountServiceMock))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    public void shouldReturnNoContentIfIncomeDataIsValid() throws Exception {
        var requestBody = this.getRequestBody(1L, 2L, 1000.00);

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
                containsInAnyOrder("sourceAccountId", "destinationAccountId", "amount")
            ))
            .andExpect(jsonPath(
                "$.errors[?(@.field=='sourceAccountId')].error",
                containsInAnyOrder("must be greater than 0")
            ))
            .andExpect(jsonPath(
                "$.errors[?(@.field=='destinationAccountId')].error",
                containsInAnyOrder("must be greater than 0")
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
        var requestBody = this.getRequestBody(1L, 2L, 1000.00);

        doThrow(new AccountNotFoundException(1L))
            .when(this.accountServiceMock)
            .internalTransfer(anyLong(), anyLong(), anyDouble());

        this.send(requestBody)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        var requestBody = this.getRequestBody(1L, 2L, 10000.00);

        doThrow(new NotEnoughMoneyException(1L, 10000.00))
            .when(this.accountServiceMock)
            .internalTransfer(anyLong(), anyLong(), anyDouble());

        this.send(requestBody)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is("notEnoughMoney")))
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private String getRequestBody(long sourceAccountId, long destinationAccountId, double amount) {
        return String.format(this.requestBodyTmpl, sourceAccountId, destinationAccountId, amount);
    }

    private ResultActions send(String requestBody) throws Exception {
        return this.send(requestBody, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(String requestBody, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(post("/transfer/internal").contentType(mediaType).content(requestBody));
    }
}
