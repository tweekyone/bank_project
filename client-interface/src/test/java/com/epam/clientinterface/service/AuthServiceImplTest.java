package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final String name = "Ivan";
    private static final String surname = "Popov";
    private static final String phoneNumber = "+79110000222";
    private static final String username = "roma_mock";
    private static final String email = "vanok@gmail.com";
    private static final String password = "123456";

    @Mock
    private UserService userService;

    @Mock
    private User mockedUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private final AuthService authService = new AuthServiceImpl();

    @Test
    void shouldSignUpWithNewEmail() {
        when(userService.create(name, surname, phoneNumber, username, email, password))
            .thenReturn(mockedUser);
        when(userRepository.existsByEmail(email))
            .thenReturn(false);

        authService.signUp(name, surname, phoneNumber, username, email, password);

        verify(userRepository).existsByEmail(eq(email));
        verify(userService).create(any(), any(), any(), any(), any(), any());
    }

    // sign up with existing email
    @Test
    void shouldThrowUserAlreadyExistException() {
        when(userRepository.existsByEmail(email))
            .thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class,
            () -> authService.signUp(
                name, surname, phoneNumber,
                username, email, password)
        );
        verify(userRepository).existsByEmail(eq(email));
    }
}