package com.epam.bank.atm.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class AuthServletTest extends BaseServletTest {
    private final HttpServletRequest requestMock;
    private final HttpServletResponse responseMock;
    private final AuthService authServiceMock;
    private final TokenSessionService tokenSessionServiceMock;
    private final AuthServlet authServlet;

    public AuthServletTest() {
        this.requestMock = mock(HttpServletRequest.class);
        this.responseMock = mock(HttpServletResponse.class);
        this.authServiceMock = mock(AuthService.class);
        this.tokenSessionServiceMock = mock(TokenSessionService.class);
        this.authServlet = new AuthServlet(this.authServiceMock, this.tokenSessionServiceMock);
    }

    @Test
    void shouldLoginIfCardNumberAndPinAreCorrect() throws IOException {
        var cardNumber = "234567";
        var pin = "2345";
        var jsonBody = String.format("{\"cardNumber\": \"%s\", \"pin\": \"%s\"}", cardNumber, pin);

        when(this.requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(this.responseMock.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(this.authServiceMock.login(cardNumber, pin))
            .thenReturn(
                new AuthDescriptor(
                    new User(1L),
                    new Account(1L, 1L, true, "plan", 10000, 1L),
                    new Card(1L, cardNumber, 1L, pin, Card.Plan.TESTPLAN, LocalDateTime.now())
                )
            );
        when(this.tokenSessionServiceMock.start(any(AuthDescriptor.class))).thenReturn("token");

        this.authServlet.doPost(this.requestMock, this.responseMock);

        verify(this.responseMock).setContentType("text/json");
        verify(this.responseMock).setStatus(204);
        verify(this.responseMock).setHeader(eq("Authorization"), anyString());
    }

    @Test
    void shouldHandleErrorIfJsonBodyIsInvalid() throws IOException {
        var jsonBody = "{asdas";
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(this.requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(this.responseMock.getWriter()).thenReturn(responseWriter);

        this.authServlet.doPost(this.requestMock, this.responseMock);

        this.assertErrorResponse(stringWriter, "invalidRequest", (short) 400, "Invalid request", "Invalid request");

        verify(this.responseMock).setContentType("text/json");
        verify(this.responseMock).setStatus(400);
    }

    @Test
    void shouldHandleErrorIfCardNumberIsEmpty() throws IOException {
        var pin = "2345";
        var jsonBody = String.format("{\"pin\": \"%s\"}", pin);
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(this.requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(this.responseMock.getWriter()).thenReturn(responseWriter);

        this.authServlet.doPost(this.requestMock, this.responseMock);

        this.assertErrorResponse(
            stringWriter,
            "cardNumberIsEmpty",
            (short) 400,
            "Number is empty",
            "Number is empty"
        );

        verify(this.responseMock).setContentType("text/json");
        verify(this.responseMock).setStatus(400);
    }

    @Test
    void shouldHandleErrorIfPinNumberIsEmpty() throws IOException {
        var cardNumber = "234567";
        var jsonBody = String.format("{\"cardNumber\": \"%s\"}", cardNumber);
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(this.requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(this.responseMock.getWriter()).thenReturn(responseWriter);

        this.authServlet.doPost(this.requestMock, this.responseMock);

        this.assertErrorResponse(stringWriter, "cardPinIsEmpty", (short) 400, "Pin is empty", "Pin is empty");

        verify(this.responseMock).setContentType("text/json");
        verify(this.responseMock).setStatus(400);
    }
}
