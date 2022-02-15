package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.config.WithMockAdmin;
import com.epam.bank.operatorinterface.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TransferController.class)
@WithMockAdmin
@AutoConfigureMockMvc(addFilters = false)
class InternalTransferControllerTest extends AbstractControllerTest {

    private final String url = "/transfer/internal";

    @MockBean
    private AccountService accountServiceMock;

    @ParameterizedTest
    @ValueSource(strings = {
        "{"
            + "\"sourceAccountId\": -1, "
            + "\"destinationAccountNumber\": \"123456789\", "
            + "\"destinationCardNumber\": \"987654321\", "
            + "\"amount\": -100"
            + "}",
        "{"
            + "\"sourceAccountId\": -1, "
            + "\"destinationAccountNumber\": null, "
            + "\"destinationCardNumber\": null, "
            + "\"amount\": -100"
            + "}"
    })
    void shouldReturnValidationErrorResponseIfRequestIsInvalid(String requestBody) throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type", is("validation")))
            .andExpect(jsonPath("$.errors[*].field",
                containsInAnyOrder("sourceAccountId", "destinationAccountNumber",
                    "destinationCardNumber", "amount")
            ))
            .andExpect(jsonPath("$.errors[?(@.field=='sourceAccountId')].error",
                hasItem("must be greater than 0")))
            .andExpect(jsonPath("$.errors[?(@.field=='destinationAccountNumber')].error",
                hasItem("invalid account number or card number")))
            .andExpect(jsonPath("$.errors[?(@.field=='destinationCardNumber')].error",
                hasItem("invalid account number or card number")))
            .andExpect(jsonPath("$.errors[?(@.field=='amount')].error",
                hasItem("must be greater than 0")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{",
        ""
    })
    void shouldReturnBadRequestIfRequestIsIncorrect(String requestBody) throws Exception {
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeIfContentTypeIsNotJson() throws Exception {
        mockMvc.perform(post(url)
            .contentType(MediaType.TEXT_HTML)
            .content("")).andExpect(status().isUnsupportedMediaType());
    }
}
