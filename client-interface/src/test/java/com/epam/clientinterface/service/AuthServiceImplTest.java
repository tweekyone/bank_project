package com.epam.clientinterface.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.impl.AuthServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final String NAME = randomAlphabetic(2, 30);
    private static final String SURNAME = randomAlphabetic(2, 30);
    private static final String PHONE_NUMBER = randomNumeric(9, 15);
    private static final String USERNAME = randomAlphanumeric(3, 20);
    private static final String EMAIL = randomAlphabetic(2, 30);
    private static final String PASSWORD = randomAlphanumeric(6, 30);

    @Mock
    private UserService userService;

    Account.AsFirstFactory firstFactory =
        new Account.AsFirstFactory(RandomStringUtils.randomNumeric(20));

    private final User mockedUser =
        new User(NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD, firstFactory);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldSignUpWithNewEmail() {
        when(userRepository.existsByEmail(EMAIL))
            .thenReturn(false);
        when(userService.create(NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD))
            .thenReturn(mockedUser);

        User user = authService.signUp(NAME, SURNAME, PHONE_NUMBER, USERNAME, EMAIL, PASSWORD);
        Assertions.assertEquals(mockedUser, user);

        verify(userRepository, times(1)).existsByEmail(eq(EMAIL));
        verify(userService, times(1)).create(any(), any(), any(), any(), any(), any());
    }

    // sign up with existing email
    @Test
    void shouldThrowUserAlreadyExistException() {
        when(userRepository.existsByEmail(EMAIL))
            .thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class,
            () -> authService.signUp(
                NAME, SURNAME, PHONE_NUMBER,
                USERNAME, EMAIL, PASSWORD)
        );
        verify(userRepository).existsByEmail(eq(EMAIL));
    }
}