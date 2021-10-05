package com.epam.bank.atm.controller;

import com.epam.bank.atm.service.AccountService;
import com.epam.bank.atm.service.AccountServiceImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/withdraw")
public class AccountController extends HttpServlet {

    private final AccountService accountService;

    public AccountController() {
        accountService = new AccountServiceImpl();
    }
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws  IOException {

        Long accountId = 10L;
        Double amount;

        response.setContentType("text/json");
        response.setCharacterEncoding("utf-8");

        try {
            JsonElement jsonBody = JsonParser.parseReader(new JsonReader(request.getReader()));
            if (jsonBody.isJsonObject()) {
                amount = jsonBody.getAsJsonObject().get("amount").getAsDouble();
            } else {
                response.sendError(400, "InValid JsonObject");
                return;
            }

            accountService.withdraw(accountId, amount);

            response.setStatus(200);
            PrintWriter writeResp = response.getWriter();
            JsonObject jsonResp = new JsonObject();
            jsonResp.addProperty("amount", amount);
            writeResp.print(jsonResp);
            writeResp.flush();
        } catch (UnsupportedEncodingException |
                 JsonParseException |
                 IllegalStateException |
                 NumberFormatException |
                 NullPointerException e) {
            response.sendError(400,  "Bad request");
        } catch (Exception e) {
            response.sendError(500, "Error service");
        }
    }
}
