package com.epam.bank.atm.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {
    // ToDo: provide middleware support (at least authentication middleware) here
    @Override
    public void init() throws ServletException {
        super.init();
    }
}
