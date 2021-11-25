package com.epam.clientinterface.controller;

import static com.epam.clientinterface.controller.util.UserTestData.USER;
import static com.epam.clientinterface.controller.util.UserTestData.USER_TO_BLOCK;
import static com.epam.clientinterface.controller.util.UserTestData.getUserView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.configuration.ApplicationConfiguration;
import com.epam.clientinterface.controller.util.AuthRequest;
import com.epam.clientinterface.domain.dto.UserDto;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringJUnitWebConfig(ApplicationConfiguration.class)
@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class AuthSecurityTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();
    private static final String LOGIN = "/login";
    private static final String HELLO = "/bank/secured/hello";

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Mock
    UserRepository mockRepository;

    @Mock
    UserService mockService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

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
    }

    @AfterEach
    void tearDown() {
        reset(mockRepository);
        reset(mockService);
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
        this.mockRepository = (UserRepository) webApplicationContext.getBean("userRepositoryMock");
        this.mockService = (UserService) webApplicationContext.getBean("userServiceMock");
    }

    private AuthRequest getAuthRequest(UserDto userDto, String password) {
        AuthRequest request = new AuthRequest();
        request.setEmail(userDto.getEmail());
        request.setPassword(password);
        return request;
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(mockService.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));

        mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk());
    }

    @Test
    void testLogin5TimesFail() throws Exception {
        UserDto userDto = getUserView(USER_TO_BLOCK);
        AuthRequest request = getAuthRequest(userDto, "PASSWORD");

        when(mockRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(USER_TO_BLOCK));
        when(mockService.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER_TO_BLOCK));
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                    .header("Authorization", "Basic YWFAZW1haWwuY29tOnNkc2RzZA=="))
                .andExpect(status().isUnauthorized());
        }
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(mockRepository, atLeastOnce()).save(captor.capture());
        User lastSavedUser = captor.getValue();
        assert (!lastSavedUser.isEnabled());
        assertEquals(5, lastSavedUser.getFailedLoginAttempts());
    }

    @Test
    void testLoginFail() throws Exception {
        mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnNkc2RzZA=="))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testHelloFailsWithoutLogin() throws Exception {
        this.mockMvc.perform(get(HELLO))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void testHelloSuccess() throws Exception {
        when(mockService.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));

        MvcResult result1 = mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk()).andReturn();
        String token = result1.getResponse().getHeader("Authorization");
        mockMvc.perform(get(HELLO).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token)))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello"));
    }
}