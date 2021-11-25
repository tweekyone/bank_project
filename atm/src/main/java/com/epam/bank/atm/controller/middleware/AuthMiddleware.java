package com.epam.bank.atm.controller.middleware;

import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.di.DiContainer;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthMiddleware implements Filter {
    private final TokenService tokenService = DiContainer.instance().getSingleton(TokenService.class);
    private final TokenSessionService sessionService = DiContainer.instance().getSingleton(TokenSessionService.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var resp = (HttpServletResponse) response;

        var token = req.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer") || token.replace("Bearer ", "").isBlank()) {
            var error = new ErrorResponse("tokenIsEmpty", (short) 401);
            resp.setStatus(401);
            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(error));
            return;
        }

        token = token.replace("Bearer ", "");

        if (this.tokenService.isExpired(token)) {
            var error = new ErrorResponse("tokenIsExpired", (short) 401);
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
