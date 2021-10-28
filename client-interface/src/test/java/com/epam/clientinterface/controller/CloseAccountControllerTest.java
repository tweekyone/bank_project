package com.epam.clientinterface.controller;


import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.CloseAccountController;
import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CloseAccountControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AccountService accountServiceMock;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new CloseAccountController(this.accountServiceMock))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    public void shouldReturnNoContentIfAccountExists() throws Exception {
        this.send(1L).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundIfAccountDoesNotExist() throws Exception {
        doThrow(AccountNotFoundException.class).when(this.accountServiceMock).closeAccount(anyLong());

        this.send(1L)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type", is("accountNotFound")))
            .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    private ResultActions send(long accountId) throws Exception {
        return this.send(accountId, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(long accountId, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(delete(String.format("/accounts/%d", accountId)).contentType(mediaType));
    }
}
