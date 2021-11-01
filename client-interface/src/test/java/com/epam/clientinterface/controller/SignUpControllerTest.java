package com.epam.clientinterface.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.configuration.SignUpControllerTestConfiguration;
import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = SignUpControllerTestConfiguration.class,
    loader = AnnotationConfigContextLoader.class)
class SignUpControllerTest {

    private MockMvc mockMvc;

    private final String signUpRequest = "{\"name\":\"Ivan\",\"surname\":\"Popov\", "
        + "\"phoneNumber\":\"+79100000\",\"username\":\"vanok\","
        + "\"email\":\"vanok@gmail.com\", \"password\":\"1234\"}";


    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new SignUpController(this.authService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @Mock
    // @Autowired
    private AuthService authService;

    @Autowired
    private SignUpController signUpController;

    @Test
    void shouldRegisterNewUserAccount() throws Exception {

        this.send(signUpRequest).andExpect(status().isCreated());

    }

    private ResultActions send(String requestBody) throws Exception {
        return this.send(requestBody, MediaType.APPLICATION_JSON);
    }

    private ResultActions send(String requestBody, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(post("/user/registration").contentType(mediaType).content(requestBody));
    }
}