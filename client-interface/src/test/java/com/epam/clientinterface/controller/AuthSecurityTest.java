package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.util.AuthRequest;
import com.epam.clientinterface.domain.dto.UserDto;
import com.epam.clientinterface.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Optional;

import static com.epam.clientinterface.controller.util.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthSecurityTest extends AbstractControllerTest {

    private static final String HELLO = "/bank/secured/hello";

    private AuthRequest getAuthRequest(UserDto userDto, String password) {
        AuthRequest request = new AuthRequest();
        request.setEmail(userDto.getEmail());
        request.setPassword(password);
        return request;
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(super.userServiceMock.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        when(super.userRepositoryMock.findByEmailWithRoles(USER.getEmail())).thenReturn(Optional.of(USER));

        mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk());
    }

    @Test
    void testLogin5TimesFail() throws Exception {
        UserDto userDto = getUserView(USER_TO_BLOCK);
        AuthRequest request = getAuthRequest(userDto, "PASSWORD");

        when(super.userRepositoryMock.findByEmail(request.getEmail())).thenReturn(Optional.of(USER_TO_BLOCK));
        when(super.userServiceMock.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER_TO_BLOCK));
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                    .header("Authorization", "Basic YWFAZW1haWwuY29tOnNkc2RzZA=="))
                .andExpect(status().isUnauthorized());
        }
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(super.userRepositoryMock, atLeastOnce()).save(captor.capture());
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
        when(super.userServiceMock.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        when(super.userRepositoryMock.findByEmailWithRoles(USER.getEmail())).thenReturn(Optional.of(USER));
        MvcResult result1 = mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk()).andReturn();
        String token = result1.getResponse().getHeader("Authorization");
        mockMvc.perform(get(HELLO).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token)))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello"));
    }
}