package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.exception.TransferException;
import com.epam.bank.operatorinterface.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import util.TestRequestFactory;

@WebMvcTest(TransferController.class)
public class InternalByCardTransferControllerTest {
    private final String url = "/transfer/internal";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountServiceMock;

    @Test
    public void shouldReturnCreatedIfRequestIsValid() throws Exception {
        doNothing().when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content((TestRequestFactory.getInternalByCardRequestBody())))
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnNotFoundIfServiceThrowsCardNotFound() throws Exception {
        doThrow(CardNotFoundException.class)
            .when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByCardRequestBody()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is(CardNotFoundException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsNotEnoughMoney() throws Exception {
        doThrow(NotEnoughMoneyException.class)
            .when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByCardRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(NotEnoughMoneyException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsAccountIsClosed() throws Exception {
        doThrow(AccountIsClosedException.class)
            .when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByCardRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsClosedException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsAccountIsNotSupposedForWithdraw() throws Exception {
        doThrow(AccountIsNotSupposedForWithdrawException.class)
            .when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByCardRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsNotSupposedForWithdrawException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfServiceThrowsTransferException() throws Exception {
        doThrow(TransferException.class)
            .when(accountServiceMock).internalTransferByCard(anyLong(), anyString(), anyDouble());

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(TestRequestFactory.getInternalByCardRequestBody()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(TransferException.class.getName())));
    }
}
