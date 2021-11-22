package com.epam.bank.atm.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.service.AccountService;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PutMoneyServletTest extends BaseServletTest {

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private PutMoneyServlet servlet;
    private AccountService accountServiceMock;
    private TokenSessionService tokenSessionServiceMock;

    @BeforeEach
    public void setUp() {
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        accountServiceMock = mock(AccountService.class);
        tokenSessionServiceMock = mock(TokenSessionService.class);
        servlet = new PutMoneyServlet(accountServiceMock, tokenSessionServiceMock);
        //inject into servlet
        Class servletClass = servlet.getClass();
        try {
            Field accountServiceField = servletClass.getDeclaredField("accountService");
            accountServiceField.setAccessible(true);
            accountServiceField.set(servlet, accountServiceMock);
            Field tokenSessionServiceField = servletClass.getDeclaredField("tokenSessionService");
            tokenSessionServiceField.setAccessible(true);
            tokenSessionServiceField.set(servlet, tokenSessionServiceMock);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ifJsonBodyIsIncorrect() throws IOException {
        String json = "{asfs}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        AuthDescriptor authDescriptor = new AuthDescriptor(
            new User(123L),
            new Account(1L, 1L, true, "plan", 10000, 1L),
            new Card(1L, "1234567890123456", 1L, "1234", Card.Plan.TESTPLAN, LocalDateTime.now())
        );

        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(resp.getWriter()).thenReturn(writer);
        when(tokenSessionServiceMock.curSession()).thenReturn(authDescriptor);

        servlet.doPost(req, resp);

        verify(resp).setStatus(400);
        writer.flush();
        Assertions.assertTrue(stringWriter.toString().contains(
            new Gson().toJson(
                new ErrorResponse("Wrong JSON format", (short) 400))));
    }

    @Test
    public void ifAmountIsNull() throws IOException {
        String json = "{\"amount\":0}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        AuthDescriptor authDescriptor = new AuthDescriptor(
            new User(123L),
            new Account(1L, 1L, true, "plan", 10000, 1L),
            new Card(1L, "1234567890123456", 1L, "1234", Card.Plan.TESTPLAN, LocalDateTime.now())
        );

        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(resp.getWriter()).thenReturn(writer);
        when(tokenSessionServiceMock.curSession()).thenReturn(authDescriptor);

        servlet.doPost(req, resp);

        verify(resp).setStatus(400);
        writer.flush();
        Assertions.assertTrue(stringWriter.toString().contains(
            new Gson().toJson(
                new ErrorResponse("Amount is not filled", (short) 400))));
    }

    @Test
    public void ifAmountIsCorrect() throws IOException {
        String json = "{\"amount\":1000000}";
        long amount = 1000000;
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        double balance = 2000000;
        AuthDescriptor authDescriptor = new AuthDescriptor(
            new User(123L),
            new Account(1L, 1L, true, "plan", 10000, 1L),
            new Card(1L, "1234567890123456", 1L, "1234", Card.Plan.TESTPLAN, LocalDateTime.now())
        );

        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(resp.getWriter()).thenReturn(writer);
        when(tokenSessionServiceMock.curSession()).thenReturn(authDescriptor);
        when(accountServiceMock.putMoney(authDescriptor.getAccount().getId(), amount)).thenReturn(balance);

        servlet.doPost(req, resp);

        verify(resp).setContentType("text/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(200);
        writer.flush();
        Assertions.assertEquals(balance, accountServiceMock.putMoney(authDescriptor.getAccount().getId(), amount));
    }

}
