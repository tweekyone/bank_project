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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final Long id = RandomUtils.nextLong();
    private static final String name = randomAlphabetic(2, 30);
    private static final String surname = randomAlphabetic(2, 30);
    private static final String phoneNumber = randomNumeric(9, 15);
    private static final String username = randomAlphanumeric(3, 20);
    private static final String email = randomAlphabetic(2, 30);
    private static final String password = randomAlphanumeric(6, 30);

    @Mock
    private UserService userService;

    List<Account> accounts = new ArrayList<>();

    private final User mockedUser =
        new User(id, name, surname, phoneNumber, username, email, password, accounts);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldSignUpWithNewEmail() {
        when(userRepository.existsByEmail(email))
            .thenReturn(false);
        when(userService.create(name, surname, phoneNumber, username, email, password))
            .thenReturn(mockedUser);

        User user = authService.signUp(name, surname, phoneNumber, username, email, password);
        Assertions.assertEquals(mockedUser, user);

        verify(userRepository, times(1)).existsByEmail(eq(email));
        verify(userService, times(1)).create(any(), any(), any(), any(), any(), any());
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