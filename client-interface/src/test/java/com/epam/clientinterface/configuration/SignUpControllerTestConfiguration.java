package com.epam.clientinterface.configuration;

import com.epam.clientinterface.controller.SignUpController;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class SignUpControllerTestConfiguration {

    @Bean
    public AuthService authService() {
        return mock(AuthService.class);
    }

    @Bean
    SignUpController signUpController() {
        return new SignUpController(authService());
    }
}
