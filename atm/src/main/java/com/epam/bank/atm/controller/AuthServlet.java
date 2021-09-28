package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends BaseServlet {
    private final AuthService authService = DIContainer.instance().getSingleton(AuthService.class);
    private final TokenSessionService sessionService = DIContainer.instance().getSingleton(TokenSessionService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getParameterMap().containsKey("login")) {
            resp.setStatus(400);
            resp.getWriter().println("Parameter login is required");
            return;
        }

        if (!req.getParameterMap().containsKey("password")) {
            resp.setStatus(400);
            resp.getWriter().println("Parameter password is required");
            return;
        }

        var login = req.getParameter("login");
        var password = req.getParameter("password");

        try {
            var authDescriptor = this.authService.login(login, password);
            var token = this.sessionService.start(authDescriptor);

            resp.setStatus(204);
            resp.setHeader("Authorization", String.format("Bearer %s", token));
        } catch (RuntimeException e) {
            resp.setStatus(401);
            resp.getWriter().println("Unauthorized");
        }
    }
}

