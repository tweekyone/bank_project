package com.epam.bank.operatorinterface.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.TestDataFactory.getUserDetailsAuth;

import com.epam.bank.operatorinterface.repository.UserRepository;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogInTest {

    private static final String LOGIN = "/login";
    private final UserDetails user = getUserDetailsAuth();

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void shouldReturnIsOkIfRequestIsCorrect() throws Exception {
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);

        String token = new String(Base64.getEncoder().encode(("test@test.com:" + "pass").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnIsUnauthorizedIfServiceThrowUsernameNotFoundException() throws Exception {
        doThrow(UsernameNotFoundException.class)
            .when(userDetailsService).loadUserByUsername("error@test.com");

        String token = new String(Base64.getEncoder().encode(("error@test.com:" + "pass").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldTReturnIsUnauthorizedIfPasswordIsIncorrect() throws Exception {
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);

        String token = new String(Base64.getEncoder().encode((user.getUsername() + ":pass1").getBytes()));

        mockMvc.perform(post(LOGIN).header("Authorization", "Basic " + token))
            .andExpect(status().isUnauthorized());
    }
}
