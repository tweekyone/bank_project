package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.controller.dto.Error;
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
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");

        if (!req.getParameterMap().containsKey("cardNumber")) {
            var error = new Error("card_number_is_empty", (short) 400, "Card number is empty", "Card number is empty");
            resp.setStatus(400);
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        if (!req.getParameterMap().containsKey("pin")) {
            var error = new Error("card_pin_is_empty", (short) 400, "Card pin is empty", "Card pin is empty");
            resp.setStatus(400);
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        var cardNumber = req.getParameter("cardNumber");
        var pin = req.getParameter("pin");

        try {
            var authDescriptor = this.authService.login(cardNumber, pin);
            var token = this.sessionService.start(authDescriptor);

            resp.setStatus(204);
            resp.setHeader("Authorization", String.format("Bearer %s", token));
        } catch (RuntimeException e) {
            var error = new Error("unauthorized", (short) 401, "Unauthorized", "Card number or password is incorrect");
            resp.setStatus(401);
            resp.getWriter().write(new Gson().toJson(error));
        }
    }
}
