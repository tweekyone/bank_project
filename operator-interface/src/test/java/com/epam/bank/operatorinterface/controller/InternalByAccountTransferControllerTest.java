package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.config.WithMockAdmin;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.exception.TransferException;
import com.epam.bank.operatorinterface.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import util.TestRequestFactory;

@WebMvcTest(TransferController.class)
@WithMockAdmin
@AutoConfigureMockMvc(addFilters = false)
class InternalByAccountTransferControllerTest extends AbstractControllerTest {

    private final String url = "/transfer/internal";

    @MockBean
    private AccountService accountServiceMock;

    @Test
    void shouldReturnCreatedIfRequestIsValid() throws Exception {
        doNothing().when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content((TestRequestFactory.getInternalByAccountRequestBody())))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundIfServiceThrowsAccountNotFound() throws Exception {
        doThrow(AccountNotFoundException.class)
            .when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByAccountRequestBody()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is(AccountNotFoundException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        doThrow(NotEnoughMoneyException.class)
            .when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByAccountRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(NotEnoughMoneyException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsAccountIsClosed() throws Exception {
        doThrow(AccountIsClosedException.class)
            .when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByAccountRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsClosedException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsAccountIsNotSupposedForWithdraw() throws Exception {
        doThrow(AccountIsNotSupposedForWithdrawException.class)
            .when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByAccountRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsNotSupposedForWithdrawException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfServiceThrowsTransferException() throws Exception {
        doThrow(TransferException.class)
            .when(accountServiceMock).internalTransferByAccount(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByAccountRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(TransferException.class.getName())));
    }
}
