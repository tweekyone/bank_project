package com.epam.bank.operatorinterface.controller;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.service.CardService;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CardController.class)
class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardServiceMock;

    @Test
    void shouldReturnNoContentIfValidRequestBodyIsProvided_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody()).andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestIfServiceCanNotFindCard_changePinCodeEndpoint() throws Exception {
        doThrow(NotFoundException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestIfCardIsBlocked_changePinCodeEndpoint() throws Exception {
        doThrow(CardIsBlockedException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfServiceFindThatPinCodeFormatIsInvalid_changePinCodeEndpoint() throws Exception {
        doThrow(InvalidPinCodeFormatException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfTooManyPinCodeChanges_changePinCodeEndpoint() throws Exception {
        doThrow(TooManyPinCodeChangesPerDayException.class).when(cardServiceMock).changePinCode(anyLong(), anyString());

        sendChangePinCode(RandomUtils.nextLong(), getChangePinCodeRequestBody())
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfRequestBodyIsEmpty_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "")
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfRequestBodyIsInvalid_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "{invalid")
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnprocessableEntityIfPinCodeIsNotProvided_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "{}")
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnprocessableEntityIfPinCodeIsInvalid_changePinCodeEndpoint() throws Exception {
        sendChangePinCode(RandomUtils.nextLong(), "{\"pinCode\": \"12qw\"}")
            .andExpect(status().isBadRequest());
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
