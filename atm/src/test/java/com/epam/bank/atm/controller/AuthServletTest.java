package com.epam.bank.atm.controller;

import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class AuthServletTest extends BaseServletTest {
    @Test
    void shouldLoginIfCardNumberAndPinAreCorrect() throws IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var authService = mock(AuthService.class);

        var cardNumber = "234567";
        var pin = "2345";
        var jsonBody = String.format("{\"cardNumber\": \"%s\", \"pin\": \"%s\"}", cardNumber, pin);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(authService.login(cardNumber, pin))
            .thenReturn(new AuthDescriptor(new User(1L), new Account(1L, 1L), new Card(1L, cardNumber, 1L, pin)));

        var servlet = new AuthServlet(authService, DIContainer.instance().getSingleton(TokenSessionService.class));
        servlet.doPost(request, response);

        verify(response).setContentType("text/json");
        verify(response).setStatus(204);
        verify(response).setHeader(eq("Authorization"), anyString());
    }

    @Test
    void shouldHandleErrorIfJsonBodyIsInvalid() throws IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var jsonBody = "{asdas";
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(responseWriter);

        new AuthServlet().doPost(request, response);

        this.assertErrorResponse(stringWriter, "invalidRequest", (short) 400, "Invalid request", "Invalid request");

        verify(response).setContentType("text/json");
        verify(response).setStatus(400);
    }

    @Test
    void shouldHandleErrorIfCardNumberIsEmpty() throws IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var pin = "2345";
        var jsonBody = String.format("{\"pin\": \"%s\"}", pin);
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(responseWriter);

        new AuthServlet().doPost(request, response);

        this.assertErrorResponse(
            stringWriter,
            "cardNumberIsEmpty",
            (short) 400,
            "Number is empty",
            "Number is empty"
        );

        verify(response).setContentType("text/json");
        verify(response).setStatus(400);
    }

    @Test
    void shouldHandleErrorIfPinNumberIsEmpty() throws IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var cardNumber = "234567";
        var jsonBody = String.format("{\"cardNumber\": \"%s\"}", cardNumber);
        var stringWriter = new StringWriter();
        var responseWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(response.getWriter()).thenReturn(responseWriter);

        new AuthServlet().doPost(request, response);

        this.assertErrorResponse(stringWriter, "cardPinIsEmpty", (short) 400, "Pin is empty", "Pin is empty");

        verify(response).setContentType("text/json");
        verify(response).setStatus(400);
    }
}
