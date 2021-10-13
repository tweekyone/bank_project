package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.service.AccountService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@WebServlet("/withdraw")
public class WithdrawMoneyServlet extends BaseServlet {

    private final AccountService accountService;
    private final TokenSessionService tokenSessionService;

    public WithdrawMoneyServlet() {
        this.accountService = DIContainer.instance().getSingleton(AccountService.class);
        this.tokenSessionService = DIContainer.instance().getSingleton(TokenSessionService.class);
    }

    public WithdrawMoneyServlet(AccountService accountService, TokenSessionService tokenSessionService) {
        this.accountService = accountService;
        this.tokenSessionService = tokenSessionService;
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        AuthDescriptor authDescriptor = tokenSessionService.curSession();

        double amount;

        try {
            JsonElement jsonBody = JsonParser.parseReader(new JsonReader(req.getReader()));
            if (jsonBody.isJsonObject()) {
                amount = jsonBody.getAsJsonObject().get("amount").getAsDouble();
            } else {
                this.sendError(resp, "Wrong JsonObject", (short) 400, "Wrong JsonObject",
                    "Wrong JsonObject");
                return;
            }

            double balance = accountService.withdrawMoney(authDescriptor.getAccount().getId(), amount);

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            PrintWriter writeResp = resp.getWriter();
            JsonObject jsonResp = new JsonObject();
            jsonResp.addProperty("balance", balance);
            writeResp.print(jsonResp);
            writeResp.flush();
        } catch (UnsupportedEncodingException |
            IllegalStateException |
            NumberFormatException |
            NullPointerException e) {
            this.sendError(resp, "Bad request", (short) 400, "Body is wrong",
                "Body is wrong");
        } catch (JsonParseException e) {
            this.sendError(resp, "Bad request", (short) 400, "Wrong JSON format",
                "Wrong JSON format");
        } catch (IllegalArgumentException e) {
            this.sendError(resp, "Bad amount", (short) 400, "Bad amount", "Amount is 0, Nan, -Inf/Inf, > account");
        } catch (Exception e) {
            this.sendError(resp, "Error service", (short) 500, "Error service", "Error service");
        }
    }
}
