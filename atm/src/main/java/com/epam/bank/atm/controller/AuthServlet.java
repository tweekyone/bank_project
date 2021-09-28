package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends BaseServlet {
    private final AuthService authService = DIContainer.instance().getSingleton(AuthService.class);
    private final TokenSessionService sessionService = DIContainer.instance().getSingleton(TokenSessionService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getParameterMap().containsKey("cardNumber")) {
            resp.setStatus(400);
            resp.getWriter().println("Parameter cardNumber is required");
            return;
        }

        if (!req.getParameterMap().containsKey("pin")) {
            resp.setStatus(400);
            resp.getWriter().println("Parameter pin is required");
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
            resp.setStatus(401);
            resp.getWriter().println("Unauthorized");
        }
    }
}
