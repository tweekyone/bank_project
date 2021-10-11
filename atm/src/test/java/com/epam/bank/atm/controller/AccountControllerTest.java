package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class AccountControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    AccountService accountService;
    AccountController accountController;
    AuthDescriptor authDescriptor;
    TokenSessionService tokenSessionService;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = mock(AccountService.class);
        tokenSessionService = mock(TokenSessionService.class);
        accountController = new AccountController(accountService, tokenSessionService);
        authDescriptor = new AuthDescriptor(
            new User(1L),
            new Account(1L, 1L),
            new Card(1L, "1234567890123456", 1L, "5555", Card.Plan.TESTPLAN, LocalDateTime.now())
        );
    }

    @Test
    public void shouldWithdrawMoneyIfAmountIsCorrect() throws Exception {
        double amount = 500.0;
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(accountService.withdrawMoney(accountId, amount)).thenReturn(5178.58);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        accountController.doPut(request, response);

        verify(response).setContentType("text/json");
        verify(response).setStatus(200);
        verify(accountService).withdrawMoney(accountId, amount);
        assertEquals(String.format("{\"balance\":%s}", 5178.58), stringWriter.toString());
    }

    //TODO: checking amount>account
    @Disabled
    @Test
    public void shouldThrowExceptionIfAmountGreaterThanAccount() throws Exception {
        double amount = 5000.0;
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);
        doThrow(new Exception("AmountGreaterThanAccount")).when(accountService).withdrawMoney(accountId, amount);

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(500, "Error service");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    public void shouldThrowExceptionIfAmountIsWrong(double arg) throws Exception {
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", arg);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);
        doThrow(new IllegalArgumentException("Less than the minimum amount")).when(accountService)
            .withdrawMoney(accountId, arg);

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(400, "Bad amount");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "test",
    })
    public void shouldErrorIfJsonBodyIsInValid(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(400, "InValid JsonObject");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{test",
        "{''amount'' : 0}",
        "{''amount'' : amount}",
        "{''amount'' : 0",
    })
    public void shouldThrowExceptionIfRequestBodyIsInValid(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(400, "Bad request");
    }

}
