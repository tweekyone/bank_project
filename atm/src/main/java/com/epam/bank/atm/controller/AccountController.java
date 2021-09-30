package com.epam.bank.atm.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.epam.bank.atm.services.AccountServiceImpl;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/withdraw")
public class AccountController extends HttpServlet {

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Long accountId = 10L;
        Double amount = null;

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        try {
            JsonElement bodyJson = JsonParser.parseReader(new JsonReader(request.getReader()));
            if (bodyJson.isJsonObject()) {
                 //accountId = bodyJson.getAsJsonObject().get("accountId").getAsLong();
                amount = bodyJson.getAsJsonObject().get("amount").getAsDouble();
            } else {
                response.sendError(400, "Wrong JsonObject");
                return;
            }

            if (amount < 0) {
                response.sendError(400, "Wrong amount");
                return;
            }
            AccountServiceImpl accountService = new AccountServiceImpl();
            accountService.withdraw(accountId, amount);

            response.setStatus(204);
            PrintWriter writeResp = response.getWriter();
            JsonObject json = new JsonObject();
            json.addProperty("amount", amount);
            writeResp.print(json);
        } catch (UnsupportedEncodingException | IllegalStateException e) {
            response.sendError(400,  e.getMessage());
        } catch (JsonParseException e) {
            response.sendError(400,  e.getMessage());
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }
}
