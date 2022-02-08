package com.epam.bank.operatorinterface.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.TestDataFactory.getUserDetailsAuth;

import com.epam.bank.operatorinterface.configuration.WebSecurityConfiguration;
import com.epam.bank.operatorinterface.configuration.security.handler.BasicAuthenticationSuccessHandler;
import com.epam.bank.operatorinterface.repository.UserRepository;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Import(WebSecurityConfiguration.class)
@WebMvcTest(LogInTest.class)
@AutoConfigureMockMvc()
class LogInTest extends AbstractControllerTest {

    private static final String LOGIN = "/login";
    private final UserDetails user = getUserDetailsAuth();

    @MockBean
    UserRepository userRepository;

    @MockBean
    BasicAuthenticationSuccessHandler handler;

    @Test
    void shouldReturnIsOkIfRequestIsCorrect() throws Exception {
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);

        String token = new String(Base64.getEncoder().encode(("test@test.com:" + "pass").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnIsUnauthorizedIfServiceThrowUsernameNotFoundException() throws Exception {
        doThrow(UsernameNotFoundException.class)
            .when(userDetailsService).loadUserByUsername("error@test.com");

        String token = new String(Base64.getEncoder().encode(("error@test.com:" + "pass").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldTReturnIsUnauthorizedIfPasswordIsIncorrect() throws Exception {
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);

        String token = new String(Base64.getEncoder().encode((user.getUsername() + ":pass1").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isUnauthorized());
    }
}
