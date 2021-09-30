package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
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

public class AuthServletTest {
    @Test
    void shouldLoginIfCardNumberAndPinAreCorrect() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthService authService = mock(AuthService.class);

        String cardNumber = "234567";
        String pin = "2345";
        String jsonBody = String.format("{\"cardNumber\": \"%s\", \"pin\": \"%s\"}", cardNumber, pin);

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
}
