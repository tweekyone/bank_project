package com.epam.clientinterface.controller;


import static com.epam.clientinterface.controller.util.UserTestData.USER;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

public class AccountControllerCloseAccountTest extends AbstractControllerTest {

    @Test
    public void shouldReturnNoContentIfAccountExists() throws Exception {
        this.send(1L).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundIfAccountDoesNotExist() throws Exception {
        doThrow(AccountNotFoundException.class).when(super.accountServiceMock).closeAccount(anyLong(), anyLong());

        this.send(1L)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    private ResultActions send(long accountId) throws Exception {
        return this.send(accountId, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(long accountId, MediaType mediaType) throws Exception {
        when(super.userServiceMock.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        when(super.userRepositoryMock.findByEmailWithRoles(USER.getEmail())).thenReturn(Optional.of(USER));

        MvcResult result1 = mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk()).andReturn();
        String token = result1.getResponse().getHeader("Authorization");

        return this.mockMvc.perform(delete(String.format("/accounts/%d", accountId))
            .header("Authorization", String.format("Bearer %s", token))
            .contentType(mediaType));
    }
}
