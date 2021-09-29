package com.epam.bank.atm.controller.middleware;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.controller.dto.Error;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.google.gson.Gson;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthMiddleware implements Filter {
    private final TokenService tokenService = DIContainer.instance().getSingleton(TokenService.class);
    private final TokenSessionService sessionService = DIContainer.instance().getSingleton(TokenSessionService.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var resp = (HttpServletResponse) response;

        var token = req.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer") || token.replace("Bearer ", "").isBlank()) {
            var error = new Error("token_is_empty", (short) 401, "Unauthorized", "Token is empty");
            resp.setStatus(401);
            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        token = token.replace("Bearer ", "");

        if (this.tokenService.isExpired(token)) {
            var error = new Error("token_is_expired", (short) 401, "Unauthorized", "Token is expired");
            resp.setStatus(401);
            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        this.sessionService.initFromToken(token);

        chain.doFilter(request, response);
    }
}
