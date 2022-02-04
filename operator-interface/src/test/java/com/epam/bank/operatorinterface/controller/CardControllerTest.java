package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.service.CardService;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardServiceMock;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnNoContentIfValidRequestBodyIsProvided_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody()).andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestIfServiceCanNotFindCard_changePinCodeEndpoint() throws Exception {
        doThrow(CardNotFoundException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type").value(CardNotFoundException.class.getName()));
    }

    @Test
    void shouldReturnBadRequestIfCardIsBlocked_changePinCodeEndpoint() throws Exception {
        doThrow(CardIsBlockedException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(CardIsBlockedException.class.getName()));
    }

    @Test
    void shouldReturnBadRequestIfServiceFindThatPinCodeFormatIsInvalid_changePinCodeEndpoint() throws Exception {
        doThrow(InvalidPinCodeFormatException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(InvalidPinCodeFormatException.class.getName()));
    }

    @Test
    void shouldReturnBadRequestIfTooManyPinCodeChanges_changePinCodeEndpoint() throws Exception {
        doThrow(TooManyPinCodeChangesPerDayException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type").value(TooManyPinCodeChangesPerDayException.class.getName()));
    }

    @Test
    void shouldReturnBadRequestIfRequestBodyIsEmpty_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(HttpMessageNotReadableException.class.getName())));
    }

    @Test
    void shouldReturnBadRequestIfRequestBodyIsInvalid_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "{invalid")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(HttpMessageNotReadableException.class.getName())));
    }

    @Test
    void shouldReturnUnprocessableEntityIfPinCodeIsNotProvided_changePinCodeEndpoint() throws Exception {
        var errTypes = new String[] {"NotBlank", "NotNull"};
        var errMsgs = new String[] {"must not be blank", "must not be null"};

        sendChangePinCode(RandomUtils.nextLong(), "{}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems("pinCode")))
            .andExpect(jsonPath("$.errors[?(@.field=='pinCode')].type", containsInAnyOrder(errTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='pinCode')].error", containsInAnyOrder(errMsgs)));
    }

    @Test
    void shouldReturnUnprocessableEntityIfPinCodeIsInvalid_changePinCodeEndpoint() throws Exception {
        var errTypes = new String[] {"Pattern"};
        var errMsgs = new String[] {"must match \"[0-9]{4}\""};

        sendChangePinCode(RandomUtils.nextLong(), "{\"pinCode\": \"12qw\"}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type").value("validation"))
            .andExpect(jsonPath("$.errors[*].field").value(hasItems("pinCode")))
            .andExpect(jsonPath("$.errors[?(@.field=='pinCode')].type", containsInAnyOrder(errTypes)))
            .andExpect(jsonPath("$.errors[?(@.field=='pinCode')].error", containsInAnyOrder(errMsgs)));
    }

    private String getChangePinCodeRequestBody() {
        return JSONObject.toJSONString(Map.of("pinCode", RandomStringUtils.randomNumeric(4)));
    }

    private ResultActions sendChangePinCode(long cardId, String requestBody) throws Exception {
        return mockMvc.perform(patch("/cards/{cardId}/pin-code", cardId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        );
    }
}
