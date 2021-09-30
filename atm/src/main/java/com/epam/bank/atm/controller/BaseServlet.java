package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet {
    protected void sendError(HttpServletResponse resp, String type, short status, String title, String detail)
        throws IOException {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        resp.getWriter().write(new Gson().toJson(new ErrorResponse(type, status, title, detail)));
    }
}
