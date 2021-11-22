package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.di.DiContainer;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.service.AccountService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/putmoney")
public class PutMoneyServlet extends HttpServlet {
    private final AccountService accountService;
    private final TokenSessionService tokenSessionService;

    public PutMoneyServlet() {
        this.accountService = DiContainer.instance().getSingleton(AccountService.class);
        this.tokenSessionService = DiContainer.instance().getSingleton(TokenSessionService.class);
    }

    public PutMoneyServlet(AccountService accountService, TokenSessionService tokenSessionService) {
        this.accountService = accountService;
        this.tokenSessionService = tokenSessionService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthDescriptor authDescriptor = tokenSessionService.curSession();
        if (authDescriptor == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        BufferedReader paramsRaw = req.getReader();
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = paramsRaw.readLine()) != null) {
            builder.append(line);
        }

        String str = builder.toString().replaceAll("\\s+", "");
        PrintWriter writer = resp.getWriter();

        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();

            Double amount = new Gson().fromJson(jsonObject.get("amount"), Double.class);

            if (amount == null || amount == 0) {
                writer.write(new Gson().toJson(new ErrorResponse("amountIsEmpty", (short) 400,
                    "Amount is not filled", "Amount is not filled")));
                resp.setStatus(400);
            } else {
                double balance = accountService.putMoney(authDescriptor.getAccount().getId(), amount);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("balance", balance);
                writer.write(new Gson().toJson(jsonResponse));
                resp.setStatus(200);
            }
        } catch (JsonSyntaxException e) {
            writer.write(new Gson().toJson(new ErrorResponse("wrongJSONFormat", (short) 400,
                "Wrong JSON format", "Wrong JSON format in \"Amount\"")));
            resp.setStatus(400);
        } catch (IllegalArgumentException e) {
            writer.write(new Gson().toJson(new ErrorResponse("IllegalArgumentException", (short) 400,
                "IllegalArgumentException", e.getMessage())));
            resp.setStatus(400);
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
