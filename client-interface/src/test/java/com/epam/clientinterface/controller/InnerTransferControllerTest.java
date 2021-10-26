package com.epam.clientinterface.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class InnerTransferControllerTest {
    private final MockMvc mockMvc;
    private final String url = "/transfer/inner";

    public InnerTransferControllerTest() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new InnerTransferController(mock(AccountService.class)))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    public void shouldReturnNoContentIfIncomeDataIsValid() throws Exception {
        var requestBody = String.format(
            "{\"sourceAccountId\":%d,\"destinationAccountId\":%d,\"amount\":%f}", 1, 2, 1000.00
        );

        this.send(requestBody).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnValidationErrorResponseIfRequestIsIncorrect() throws Exception {
        this.send("{}")
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type", is("validation")))
            .andExpect(jsonPath("$.status", is(422)))
            .andExpect(jsonPath("$.errors.amount", contains("must be greater than 0")))
            .andExpect(jsonPath("$.errors.sourceAccountId", contains("must be greater than 0")))
            .andExpect(jsonPath("$.errors.destinationAccountId", contains("must be greater than 0")));
    }

    @Test
    public void shouldReturnEmptyBadRequestIfRequestIsInvalid() throws Exception {
        this.send("{invalid").andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        this.send("", MediaType.TEXT_HTML).andExpect(status().isUnsupportedMediaType());
    }

    private ResultActions send(String requestBody) throws Exception {
        return this.send(requestBody, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(String requestBody, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(post(this.url).contentType(mediaType).content(requestBody));
    }
}
