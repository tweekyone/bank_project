package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.config.WithMockAdmin;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import util.TestRequestFactory;

@WebMvcTest(TransferController.class)
@WithMockAdmin
@AutoConfigureMockMvc(addFilters = false)
class ExternalTransferControllerTest extends AbstractControllerTest {

    private final String url = "/transfer/external";

    @MockBean
    private AccountService accountServiceMock;

    @ParameterizedTest
    @ValueSource(strings = {
        "{"
            + "\"sourceAccountId\": -1, "
            + "\"destinationAccountNumber\": null, "
            + "\"amount\": -100"
            + "}",
    })
    void shouldReturnValidationErrorResponseIfRequestIsInvalid(String requestBody) throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type", is("validation")))
            .andExpect(jsonPath("$.errors[*].field",
                containsInAnyOrder("sourceAccountId", "destinationAccountNumber", "amount")
            ))
            .andExpect(jsonPath("$.errors[?(@.field=='sourceAccountId')].error",
                hasItem("must be greater than 0")))
            .andExpect(jsonPath("$.errors[?(@.field=='destinationAccountNumber')].error",
                hasItem("must not be blank")))
            .andExpect(jsonPath("$.errors[?(@.field=='amount')].error",
                hasItem("must be greater than 0")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{",
        ""
    })
    void shouldReturnBadRequestIfRequestIsIncorrect(String requestBody) throws Exception {
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        mockMvc.perform(post(url)
            .contentType(MediaType.TEXT_HTML)
            .content("")).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturnCreatedIfRequestIsValid() throws Exception {
        doNothing().when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content((TestRequestFactory.getExternalRequestBody())))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsCardNotFound() throws Exception {
        doThrow(CardNotFoundException.class)
            .when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getExternalRequestBody()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is(CardNotFoundException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        doThrow(NotEnoughMoneyException.class)
            .when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getExternalRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(NotEnoughMoneyException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsAccountIsClosed() throws Exception {
        doThrow(AccountIsClosedException.class)
            .when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getExternalRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsClosedException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsAccountIsNotSupposedForWithdraw() throws Exception {
        doThrow(AccountIsNotSupposedForWithdrawException.class)
            .when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getExternalRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsNotSupposedForWithdrawException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsAccountIsNotSupposedForExternal() throws Exception {
        doThrow(AccountIsNotSupposedForExternalTransferException.class)
            .when(accountServiceMock).externalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getExternalRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsNotSupposedForExternalTransferException.class.getName())));
    }
}
