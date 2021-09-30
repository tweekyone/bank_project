package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.controller.dto.response.ErrorResponse;
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
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");

        var loginRequest = new Gson().fromJson(req.getReader(), LoginRequest.class);

        if (loginRequest.getCardNumber() == null || loginRequest.getCardNumber().isBlank()) {
            var error = new ErrorResponse(
                "card_number_is_empty",
                (short) 400,
                "Card number is empty",
                "Card number is empty"
            );
            resp.setStatus(400);
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        if (loginRequest.getPin() == null || loginRequest.getPin().isBlank()) {
            var error = new ErrorResponse(
                "card_pin_is_empty",
                (short) 400,
                "Card pin is empty",
                "Card pin is empty"
            );
            resp.setStatus(400);
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        try {
            var authDescriptor = this.authService.login(loginRequest.getCardNumber(), loginRequest.getPin());
            var token = this.sessionService.start(authDescriptor);

            resp.setStatus(204);
            resp.setHeader("Authorization", String.format("Bearer %s", token));
        } catch (RuntimeException e) {
            var error = new ErrorResponse(
                "unauthorized",
                (short) 401,
                "Unauthorized",
                "Card number or password is incorrect"
            );
            resp.setStatus(401);
            resp.getWriter().write(new Gson().toJson(error));
        }
    }
}
