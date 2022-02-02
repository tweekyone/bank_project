package com.epam.bank.operatorinterface.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.controller.mapper.AccountMapper;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.enumerated.AccountPlan;
import com.epam.bank.operatorinterface.exception.AccountCanNotBeClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.AccountNumberGenerationTriesLimitException;
import com.epam.bank.operatorinterface.exception.UserNotFoundException;
import com.epam.bank.operatorinterface.service.AccountService;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import util.TestDataFactory;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountServiceMock;

    @MockBean
    private AccountMapper responseMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void shouldReturnCreatedIfValidRequestBodyIsProvided_createEndpoint() throws Exception {
        var accountFixture = TestDataFactory.getAccount();
        var accountResponseFixture = TestDataFactory.getAccountResponse();

        when(accountServiceMock.create(anyLong(), any(AccountPlan.class))).thenReturn(accountFixture);
        when(responseMapper.map(any(Account.class))).thenReturn(accountResponseFixture);

        sendCreate(getCreateRequestBody())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(accountResponseFixture.getId())))
            .andExpect(jsonPath("$.number", is(accountResponseFixture.getNumber())))
            .andExpect(jsonPath("$.plan", is(accountResponseFixture.getPlan())))
            .andExpect(jsonPath("$.amount").value(is(accountResponseFixture.getAmount()), Double.class))
            .andExpect(jsonPath("$.userId", is(accountResponseFixture.getUserId())))
            .andExpect(jsonPath("$.cards", is(accountResponseFixture.getCards())))
            .andExpect(jsonPath("$.closedAt", is(accountResponseFixture.getClosedAt())))
            .andExpect(jsonPath("$.default", is(accountResponseFixture.isDefault())));
    }

    @Test
    public void shouldReturnNotFoundIfServiceCanNotFoundUser_createEndpoint() throws Exception {
        when(accountServiceMock.create(anyLong(), any(AccountPlan.class))).thenThrow(UserNotFoundException.class);

        sendCreate(getCreateRequestBody())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is(UserNotFoundException.class.getName())));
    }

    @Test
    public void shouldReturnInternalServerErrorIfAccountNumberGenerationFailed_createEndpoint() throws Exception {
        when(accountServiceMock.create(anyLong(), any(AccountPlan.class)))
            .thenThrow(AccountNumberGenerationTriesLimitException.class);

        sendCreate(getCreateRequestBody())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.type", is(AccountNumberGenerationTriesLimitException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsEmpty_createEndpoint() throws Exception {
        sendCreate("")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(HttpMessageNotReadableException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyIsInvalid_createEndpoint() throws Exception {
        sendCreate("{invalidRequest")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(HttpMessageNotReadableException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestWithValidationErrorsIfRequestBodyIsIncorrect_createEndpoint() throws Exception {
        sendCreate("{}")
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.type", is("validation")))
            .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("plan", "userId")))
            .andExpect(jsonPath("$.errors[?(@.field=='userId')].type", containsInAnyOrder("Positive")))
            .andExpect(jsonPath("$.errors[?(@.field=='userId')].error", containsInAnyOrder("must be greater than 0")))
            .andExpect(jsonPath("$.errors[?(@.field=='plan')].type", containsInAnyOrder("NotNull")))
            .andExpect(jsonPath("$.errors[?(@.field=='plan')].error", containsInAnyOrder("must not be null")));
    }

    @Test
    public void shouldReturnBadRequestIfRequestBodyContainsUnsupportedPlan_createEndpoint() throws Exception {
        sendCreate("{\"plan\": \"invalidPlan\"}")
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(HttpMessageNotReadableException.class.getName())));
    }

    @Test
    public void shouldReturnNoContentIfServiceReturnsNothing_makeDefaultEndpoint() throws Exception {
        sendMakeDefault(RandomUtils.nextLong()).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundIfServiceCanNotFoundAccount_makeDefaultEndpoint() throws Exception {
        doThrow(AccountNotFoundException.class).when(accountServiceMock).makeDefault(anyLong());

        sendMakeDefault(RandomUtils.nextLong())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is(AccountNotFoundException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfAccountIsClosed_makeDefaultEndpoint() throws Exception {
        doThrow(AccountIsClosedException.class).when(accountServiceMock).makeDefault(anyLong());

        sendMakeDefault(RandomUtils.nextLong())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsClosedException.class.getName())));
    }

    @Test
    public void shouldReturnNoContentIfServiceReturnsNothing_closeEndpoint() throws Exception {
        sendClose(RandomUtils.nextLong()).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestIfAccountIsClosed_closeEndpoint() throws Exception {
        doThrow(AccountIsClosedException.class).when(accountServiceMock).close(anyLong());

        sendClose(RandomUtils.nextLong())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountIsClosedException.class.getName())));
    }

    @Test
    public void shouldReturnBadRequestIfAccountCanNotBeClosed_closeEndpoint() throws Exception {
        doThrow(AccountCanNotBeClosedException.class).when(accountServiceMock).close(anyLong());

        sendClose(RandomUtils.nextLong())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.type", is(AccountCanNotBeClosedException.class.getName())));
    }

    private String getCreateRequestBody() {
        return getCreateRequestBody(
            RandomUtils.nextLong(), AccountPlan.values()[RandomUtils.nextInt(0, AccountPlan.values().length)]
        );
    }

    private String getCreateRequestBody(long userId, AccountPlan plan) {
        return JSONObject.toJSONString(Map.of("userId", userId, "plan", plan));
    }

    private ResultActions sendCreate(String requestBody) throws Exception {
        return mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody));
    }

    private ResultActions sendMakeDefault(long id) throws Exception {
        return mockMvc.perform(
            patch(String.format("/accounts/%d/set-as-default", id)).contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions sendClose(long id) throws Exception {
        return mockMvc.perform(delete(String.format("/accounts/%d", id)).contentType(MediaType.APPLICATION_JSON));
    }
}
