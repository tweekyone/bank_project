package com.epam.clientinterface.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AccountControllerCloseAccountTest extends AbstractControllerTest {

    private final String uri = "/accounts/%d";

    @Test
    public void shouldReturnNoContentIfAccountExists() throws Exception {
        send(MediaType.APPLICATION_JSON, "", HttpMethod.DELETE, String.format(uri, 1))
            .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundIfAccountDoesNotExist() throws Exception {
        doThrow(AccountNotFoundException.class).when(super.accountServiceMock).closeAccount(anyLong(), anyLong());

        send(MediaType.APPLICATION_JSON, "", HttpMethod.DELETE, String.format(uri, 1))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }
}
