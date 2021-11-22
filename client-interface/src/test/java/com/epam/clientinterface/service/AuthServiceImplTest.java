package com.epam.clientinterface.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
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

    private static final String name = randomAlphabetic(2, 30);
    private static final String surname = randomAlphabetic(2, 30);
    private static final String phoneNumber = randomNumeric(9, 15);
    private static final String username = randomAlphanumeric(3, 20);
    private static final String email = randomAlphabetic(2, 30);
    private static final String password = randomAlphanumeric(6, 30);

    @Mock
    private UserService userService;

    @Mock
    private User mockedUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private final AuthService authService =
        new AuthServiceImpl(userService, userRepository);

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