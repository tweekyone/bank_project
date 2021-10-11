package com.epam.bank.atm.controller;

import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.service.AccountService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@WebServlet("/withdraw")
public class AccountController extends HttpServlet {

    private final AccountService accountService;
    private final TokenSessionService tokenSessionService;

    public AccountController() {
        accountService = new AccountService();
        tokenSessionService = DIContainer.instance().getSingleton(TokenSessionService.class);
    }

    public AccountController(AccountService accountService, TokenSessionService tokenSessionService) {
        this.accountService = accountService;
        this.tokenSessionService = tokenSessionService;
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        AuthDescriptor authDescriptor = tokenSessionService.curSession();

        response.setContentType("text/json");
        response.setCharacterEncoding("utf-8");
        double amount;

        try {
            JsonElement jsonBody = JsonParser.parseReader(new JsonReader(request.getReader()));
            if (jsonBody.isJsonObject()) {
                amount = jsonBody.getAsJsonObject().get("amount").getAsDouble();
            } else {
                response.sendError(400, "InValid JsonObject");
                return;
            }

            double balance = accountService.withdrawMoney(authDescriptor.getAccount().getId(), amount);

            response.setStatus(200);
            PrintWriter writeResp = response.getWriter();
            JsonObject jsonResp = new JsonObject();
            jsonResp.addProperty("balance", balance);
            writeResp.print(jsonResp);
            writeResp.flush();
        } catch (UnsupportedEncodingException |
            JsonParseException |
            IllegalStateException |
            NumberFormatException |
            NullPointerException e) {
            response.sendError(400, "Bad request");
        } catch (IllegalArgumentException e) {
            response.sendError(400, "Bad amount");
        } catch (Exception e) {
            response.sendError(500, "Error service");
        }
    }
}
