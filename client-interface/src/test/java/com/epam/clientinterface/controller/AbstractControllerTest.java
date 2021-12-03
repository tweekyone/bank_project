package com.epam.clientinterface.controller;

import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.epam.clientinterface.configuration.ApplicationConfiguration;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AccountService;
import com.epam.clientinterface.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringJUnitWebConfig(ApplicationConfiguration.class)
@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
public abstract class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();
    static final String LOGIN = "/login";

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Mock
    AccountService accountServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Configuration
    static class Config {
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
    }

    @AfterEach
    void tearDown() {
        reset(userRepositoryMock);
        reset(userServiceMock);
        reset(accountServiceMock);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // https://stackoverflow.com/a/31843799/13721689
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilter(CHARACTER_ENCODING_FILTER)
            .apply(springSecurity())
            .build();
        this.userRepositoryMock = (UserRepository) webApplicationContext.getBean("userRepositoryMock");
        this.userServiceMock = (UserService) webApplicationContext.getBean("userServiceMock");
        this.accountServiceMock = (AccountService) webApplicationContext.getBean("accountServiceMock");
    }
}
