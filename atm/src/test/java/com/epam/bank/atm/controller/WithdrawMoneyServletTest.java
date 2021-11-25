package com.epam.bank.atm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.service.AccountService;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WithdrawMoneyServletTest extends BaseServletTest {

    HttpServletRequest request;
    HttpServletResponse response;
    AccountService accountService;
    WithdrawMoneyServlet withdrawMoneyServlet;
    AuthDescriptor authDescriptor;
    TokenSessionService tokenSessionService;
    StringWriter stringWriter;
    PrintWriter writer;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = mock(AccountService.class);
        tokenSessionService = mock(TokenSessionService.class);
        withdrawMoneyServlet = new WithdrawMoneyServlet(accountService, tokenSessionService);
        authDescriptor = new AuthDescriptor(
            new User(1L),
            new Account(1L, 1L, true, "plan", 10000, 1L),
            new Card(1L, "1234567890123456", 1L, "5555", Card.Plan.TESTPLAN, LocalDateTime.now())
        );
    }

    @Test
    public void shouldWithdrawMoneyIfAmountIsCorrect() throws Exception {
        double amount = 500.0;
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(writer);
        when(accountService.withdrawMoney(accountId, amount)).thenReturn(5178.58);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);

        withdrawMoneyServlet.doPut(request, response);

        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
        verify(accountService).withdrawMoney(accountId, amount);
        assertEquals(String.format("{\"balance\":%s}", 5178.58), stringWriter.toString());
    }

    @Test
    public void shouldThrowExceptionIfAmountGreaterThanAccount() throws Exception {
        double amount = 5000.0;
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(writer);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);
        doThrow(new IllegalArgumentException("Less than the current balance")).when(accountService)
            .withdrawMoney(accountId, amount);

        withdrawMoneyServlet.doPut(request, response);
        this.assertErrorResponse(stringWriter, "badAmount", (short) 400, "Bad amount",
            "Amount is 0, Nan, -Inf/Inf, > account");
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(400);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    public void shouldThrowExceptionIfAmountIsWrong(double arg) throws Exception {
        long accountId = authDescriptor.getAccount().getId();
        var jsonBody = String.format("{\"amount\":%s}", arg);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(writer);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);
        doThrow(new IllegalArgumentException("Less than the minimum amount")).when(accountService)
            .withdrawMoney(accountId, arg);

        withdrawMoneyServlet.doPut(request, response);
        this.assertErrorResponse(stringWriter, "badAmount", (short) 400, "Bad amount",
            "Amount is 0, Nan, -Inf/Inf, > account");
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "test",
    })
    public void shouldErrorIfJsonObjectIsWrong(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));
        when(response.getWriter()).thenReturn(writer);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);

        withdrawMoneyServlet.doPut(request, response);
        this.assertErrorResponse(stringWriter, "invalidJSON", (short) 400, "Invalid Json", "Invalid Json");
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{test",
        "{''amount : 0}",
        "{''amount'' : 0",
    })
    public void shouldErrorIfJsonBodyIsWrong(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));
        when(response.getWriter()).thenReturn(writer);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);

        withdrawMoneyServlet.doPut(request, response);
        this.assertErrorResponse(stringWriter, "invalidJSON", (short) 400, "Invalid Json", "Invalid Json");
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{''amount'' : amount}",
        "{''summa'' : 0}",
    })
    public void shouldErrorIfBodyIsWrong(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));
        when(response.getWriter()).thenReturn(writer);
        when(tokenSessionService.curSession()).thenReturn(authDescriptor);

        withdrawMoneyServlet.doPut(request, response);
        this.assertErrorResponse(stringWriter, "invalidBody", (short) 400, "Invalid body", "Invalid body");
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(400);
    }

}
