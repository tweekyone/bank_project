package com.epam.bank.atm.controller;


import com.epam.bank.atm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
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

public class AccountControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    AccountService accountService;
    AccountController accountController;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
    }

    @Test
    public void shouldWithdrawMoneyIfAmountIsCorrect() throws Exception
    {
        Double amount = 500.0;
        Long accountId = 10L;
        var jsonBody = String.format("{\"accountId\":%s, \"amount\":%s}", accountId, amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        doNothing().when(accountService).withdraw(accountId, amount);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        accountController.doPut(request, response);

        verify(response).setContentType("text/json");
        verify(response).setStatus(200);
        verify(accountService).withdraw(accountId, amount);
        assertEquals(String.format("{\"amount\":%s}", amount), stringWriter.toString());
    }

    @Test
    public void shouldThrowExceptionIfAmountGreaterThanAccount() throws Exception
    {
        Double amount = 5000.0;
        Long accountId = 10L;
        var jsonBody = String.format("{\"accountId\":%s, \"amount\":%s}", accountId, amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        doThrow(new Exception("AmountGreaterThanAccount")).when(accountService).withdraw(accountId, amount);

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(500,"Error service");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    public void shouldThrowExceptionIfAmountIsWrong(double arg) throws Exception {
        Double amount = arg;
        Long accountId = 10L;
        var jsonBody = String.format("{\"accountId\":%s, \"amount\":%s}", accountId, amount);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        doThrow(new Exception("Wrong amount")).when(accountService).withdraw(accountId, amount);

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(500,"Error service");
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
        verify(response).sendError(400,"InValid JsonObject");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{test",
        "{''accountId'' : account, ''amount'' : 0}",
        "{''accountId'' : 1, ''amount'' : amount}",
        "{''accountId'' : account, ''amount'' : amount}",
        "{''accountIdId'' : account, ''amount'' : 0}",
        "{''accountIdId'' : account, ''amountId'' : 0}",
        "{''amount'' : 0}",
        "{''accountId'' : 0}",
    })
    public void shouldThrowExceptionIfRequestBodyIsInValid(String jsonBody) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody.replace("''", "\""))));

        accountController.doPut(request, response);
        verify(response).setContentType("text/json");
        verify(response).setCharacterEncoding("utf-8");
        verify(response).sendError(400,"Bad request");
    }

}
