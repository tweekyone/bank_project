package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.dto.request.LoginRequest;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.di.DiContainer;
import com.epam.bank.atm.service.AuthService;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet extends BaseServlet {
    private final AuthService authService;
    private final TokenSessionService sessionService;

    public AuthServlet() {
        this.authService = DiContainer.instance().getSingleton(AuthService.class);
        this.sessionService = DiContainer.instance().getSingleton(TokenSessionService.class);
    }

    public AuthServlet(AuthService authService, TokenSessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var loginRequest = new Gson().fromJson(req.getReader(), LoginRequest.class);

            if (loginRequest.getCardNumber() == null || loginRequest.getCardNumber().isBlank()) {
                this.sendError(resp, "cardNumberIsEmpty", (short) 400, "Number is empty", "Number is empty");
                return;
            }

            if (loginRequest.getPin() == null || loginRequest.getPin().isBlank()) {
                this.sendError(resp, "cardPinIsEmpty", (short) 400, "Pin is empty", "Pin is empty");
                return;
            }

            var authDescriptor = this.authService.login(loginRequest.getCardNumber(), loginRequest.getPin());
            var token = this.sessionService.start(authDescriptor);

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(204);
            resp.setHeader("Authorization", String.format("Bearer %s", token));
        } catch (JsonSyntaxException | JsonIOException e) {
            this.sendError(resp, "invalidRequest", (short) 400, "Invalid request", "Invalid request");
        } catch (RuntimeException e) {
            this.sendError(resp, "unauthorized", (short) 401, "Unauthorized", "Number or pin is incorrect");
        }
    }
}
