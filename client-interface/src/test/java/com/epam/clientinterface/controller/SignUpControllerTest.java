package com.epam.clientinterface.controller;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

    private static final String name = randomAlphabetic(2, 30);
    private static final String surname = randomAlphabetic(2, 30);
    private static final String phoneNumber = randomNumeric(9, 15);
    private static final String username = randomAlphanumeric(3, 20);
    private static final String password = randomAlphanumeric(6, 30);

    // example "vanok@gmail.com";
    private static final String email = randomAlphanumeric(2, 20) + "@"
        + randomAlphanumeric(2, 20) + "." + randomAlphabetic(2,5);

    private final String signUpUserData = String.format(
        "{\"name\":\"%s\",\"surname\":\"%s\", "
            + "\"phoneNumber\":\"+%s\",\"username\":\"%s\","
            + "\"email\":\"%s\", \"password\":\"%s\"}",
        name, surname, phoneNumber, username, email, password
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
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(signUpUserData))
            .andExpect(status().isCreated())
            .andReturn();
    }

    @Test
    void getRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect(status().isOk())
            .andExpect(content().string("registration"));
    }

}