package com.epam.clientinterface.controller;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.service.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    private final String url = "/user/registration";

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    private static final String NAME = randomAlphabetic(2, 30);
    private static final String SURNAME = randomAlphabetic(2, 30);
    private static final String PHONE_NUMBER = "+" + randomNumeric(9, 15);
    private static final String USERNAME = randomAlphanumeric(3, 20);
    private static final String PASSWORD = randomAlphanumeric(6, 30);

    // example "vanok@gmail.com";
    private static final String EMAIL = randomAlphanumeric(2, 20) + "@"
        + randomAlphanumeric(2, 20) + "." + randomAlphabetic(2, 5);

    private static final Account.AsFirstFactory ACCOUNT_FACTORY =
        new Account.AsFirstFactory(RandomStringUtils.randomNumeric(19));

    private static final User NEW_USER =
        new User(NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD, ACCOUNT_FACTORY);

    private final String signUpUserData = String.format(
        "{\"name\":\"%s\",\"surname\":\"%s\", "
            + "\"phoneNumber\":\"%s\",\"username\":\"%s\","
            + "\"email\":\"%s\", \"password\":\"%s\"}",
        NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD
    );

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new SignUpController(authService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Test
    void shouldRegisterNewUserAccount() throws Exception {
        when(authService.signUp(NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD))
            .thenReturn(NEW_USER);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpUserData))
            .andExpect(status().isCreated())
            .andReturn();

        verify(authService).signUp(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void shouldReturnBadRequestIfUserAlreadyExist() throws Exception {
        Mockito.doThrow(UserAlreadyExistException.class)
            .when(authService)
            .signUp(any(), any(), any(), any(), any(), any());

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpUserData))
            .andExpect(status().isBadRequest());

        verify(authService).signUp(any(), any(), any(), any(), any(), any());
    }

    @Test
    void getRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isOk())
            .andExpect(content().string("registration"));
    }

}