package com.epam.clientinterface.controller;

import static com.epam.clientinterface.controller.util.UserTestData.getUserView;
import static com.epam.clientinterface.controller.util.UserTestData.user;
import static com.epam.clientinterface.controller.util.UserTestData.userToBlock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.util.AuthRequest;
import com.epam.clientinterface.domain.dto.UserDto;
import com.epam.clientinterface.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;

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
        when(super.userServiceMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(super.userRepositoryMock.findByEmailWithRoles(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk());
    }

    @Test
    void testLogin5TimesFail() throws Exception {
        UserDto userDto = getUserView(userToBlock);
        AuthRequest request = getAuthRequest(userDto, "PASSWORD");

        when(super.userRepositoryMock.findByEmail(request.getEmail())).thenReturn(Optional.of(userToBlock));
        when(super.userServiceMock.findByEmail(user.getEmail())).thenReturn(Optional.of(userToBlock));
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
        when(super.userServiceMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(super.userRepositoryMock.findByEmailWithRoles(user.getEmail())).thenReturn(Optional.of(user));
        MvcResult result1 = mockMvc.perform(get(LOGIN).servletPath(LOGIN)
                .header("Authorization", "Basic YWFAZW1haWwuY29tOnBhc3M="))
            .andExpect(status().isOk()).andReturn();
        String token = result1.getResponse().getHeader("Authorization");
        mockMvc.perform(get(HELLO).header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token)))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello"));
    }
}