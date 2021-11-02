package com.epam.clientinterface.controller;

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

    private final String signUpUserData = "{\"name\":\"Ivan\",\"surname\":\"Popov\", "
        + "\"phoneNumber\":\"+79100000\",\"username\":\"vanok\","
        + "\"email\":\"vanok@gmail.com\", \"password\":\"1234\"}";


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