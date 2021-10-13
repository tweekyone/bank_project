package com.epam.bank.atm.controller;

import com.epam.bank.atm.BaseTest;
import com.epam.bank.atm.controller.dto.response.ErrorResponse;
import com.google.gson.Gson;
import java.io.StringWriter;
import org.junit.jupiter.api.Assertions;

abstract class BaseServletTest extends BaseTest {
    protected void assertErrorResponse(StringWriter sw, String type, short status, String title, String detail) {
        var expectedError = new ErrorResponse(type, status, title, detail);
        var actualError = new Gson().fromJson(sw.toString(), ErrorResponse.class);

        Assertions.assertEquals(expectedError.getType(), actualError.getType());
        Assertions.assertEquals(expectedError.getStatus(), actualError.getStatus());
        Assertions.assertEquals(expectedError.getTitle(), actualError.getTitle());
        Assertions.assertEquals(expectedError.getDetail(), actualError.getDetail());
    }
}
