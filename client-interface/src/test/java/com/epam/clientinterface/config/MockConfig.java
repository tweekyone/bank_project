package com.epam.clientinterface.config;

import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AccountService;
import com.epam.clientinterface.service.CardService;
import com.epam.clientinterface.service.UserService;
import liquibase.integration.spring.SpringLiquibase;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockConfig {
    @Bean
    @Primary
    public UserRepository userRepositoryMock() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public UserService userServiceMock() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public AccountService accountServiceMock() {
        return Mockito.mock(AccountService.class);
    }

    @Bean
    @Primary
    public CardService cardServiceMock() {
        return Mockito.mock(CardService.class);
    }

    @Bean
    @Primary
    public SpringLiquibase liquibase() {
        return null;
    }
}
