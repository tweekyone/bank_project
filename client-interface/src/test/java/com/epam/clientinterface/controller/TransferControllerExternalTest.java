package com.epam.clientinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class TransferControllerExternalTest extends AbstractControllerTest {

    private final String uri = "/transfer/external";

    @Test
    void shouldReturnNoContentIfIncomeDataIsValid() throws Exception {
        var requestBody = this.getRequestBody(
            1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
        );

        send(MediaType.APPLICATION_JSON, requestBody, HttpMethod.POST, uri)
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnValidationErrorResponseIfRequestIsIncorrect() throws Exception {
        send(MediaType.APPLICATION_JSON, "{}", HttpMethod.POST, uri)
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
    void shouldReturnEmptyBadRequestIfRequestIsInvalid() throws Exception {
        send(MediaType.APPLICATION_JSON,"{invalid", HttpMethod.POST, uri)
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        send(MediaType.TEXT_HTML, "", HttpMethod.POST, uri)
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsAccountNotFound() throws Exception {
        var requestBody = this.getRequestBody(
            1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
        );

        Mockito.doThrow(new AccountNotFoundException(1L))
            .when(this.accountServiceMock)
            .externalTransfer(anyLong(), anyString(), anyDouble(), anyLong());

        send(MediaType.APPLICATION_JSON, requestBody, HttpMethod.POST, uri)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        var requestBody = this.getRequestBody(
            1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
        );

        Mockito.doThrow(new NotEnoughMoneyException(1L, 10000.00))
            .when(this.accountServiceMock)
            .externalTransfer(anyLong(), anyString(), anyDouble(), anyLong());

        send(MediaType.APPLICATION_JSON, requestBody, HttpMethod.POST, uri)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is("notEnoughMoney")))
            .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private String getRequestBody(long sourceAccountId, String destinationAccountNumber, double amount) {
        return JSONObject.toJSONString(Map.of(
            "sourceAccountId", sourceAccountId, "destinationAccountNumber", destinationAccountNumber, "amount", amount
        ));
    }
}
