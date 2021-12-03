package com.epam.bank.atm.controller;

import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.google.gson.Gson;
import java.io.StringWriter;
import org.junit.jupiter.api.Assertions;

abstract class BaseServletTest {
    protected void assertErrorResponse(StringWriter sw, String type, short status) {
        var expectedError = new ErrorResponse(type, status);
        var actualError = new Gson().fromJson(sw.toString(), ErrorResponse.class);

        Assertions.assertEquals(expectedError.getType(), actualError.getType());
        Assertions.assertEquals(expectedError.getStatus(), actualError.getStatus());
    }
}
