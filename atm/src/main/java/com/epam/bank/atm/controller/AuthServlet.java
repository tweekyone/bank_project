package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.controller.dto.request.LoginRequest;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.service.AuthService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends BaseServlet {
    private final AuthService authService = DIContainer.instance().getSingleton(AuthService.class);
    private final TokenSessionService sessionService = DIContainer.instance().getSingleton(TokenSessionService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var loginRequest = new Gson().fromJson(req.getReader(), LoginRequest.class);

        if (loginRequest.getCardNumber() == null || loginRequest.getCardNumber().isBlank()) {
            this.sendError(resp, "card_number_is_empty", (short) 400, "Number is empty", "Number is empty");
            return;
        }

        if (loginRequest.getPin() == null || loginRequest.getPin().isBlank()) {
            this.sendError(resp, "card_pin_is_empty", (short) 400, "Pin is empty", "Pin is empty");
            return;
        }

        try {
            var authDescriptor = this.authService.login(loginRequest.getCardNumber(), loginRequest.getPin());
            var token = this.sessionService.start(authDescriptor);

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(204);
            resp.setHeader("Authorization", String.format("Bearer %s", token));
        } catch (RuntimeException e) {
            this.sendError(resp, "unauthorized", (short) 401, "Unauthorized", "Number or pin is incorrect");
        }
    }
}
