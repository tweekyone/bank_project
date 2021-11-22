package com.epam.clientinterface.controller;

import com.epam.bank.clientinterface.controller.util.JsonHelper;
import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ChangePinCardControllerTest {
    private static final String CHANGEPASSWORD = "/cards/change-password";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ChangePinRequest testChangePinRequest;

    @Mock
    private CardService cardServiceMock;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new CardController(cardServiceMock))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
        objectMapper = new ObjectMapper();
        testChangePinRequest = new ChangePinRequest(RandomUtils.nextLong(), "1234", "1235");
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGEPASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(objectMapper, testChangePinRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGEPASSWORD)
                .contentType(MediaType.TEXT_HTML)
                .content(JsonHelper.toJson(objectMapper, testChangePinRequest)))
            .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturnUnprocessableEntity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGEPASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGEPASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{badRequest}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}