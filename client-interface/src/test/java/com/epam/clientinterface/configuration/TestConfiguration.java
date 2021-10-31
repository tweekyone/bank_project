package com.epam.clientinterface.configuration;

import static org.mockito.Mockito.mock;

import com.epam.clientinterface.controller.SignUpController;
import com.epam.clientinterface.controller.validator.EmailValidator;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import com.epam.clientinterface.service.impl.AuthServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public SignUpController signUpController() {
        return mock(SignUpController.class);
    }

    @Bean
    public EmailValidator emailValidator() {
        return new EmailValidator();
    }

    @Bean
    public AuthService authService() {
        return new AuthServiceImpl();
    }

    @Bean
    UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    UserRepository userRepository() {
        return mock(UserRepository.class);
    }
}
