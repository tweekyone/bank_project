package com.epam.bank.atm.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthServiceTest {
    private AccountRepository mockAccountRepository;
    private CardRepository mockCardRepository;
    private UserRepository mockUserRepository;

    private AuthService authService;

    @BeforeEach
    public void init() {
        mockAccountRepository = mock(AccountRepository.class);
        mockCardRepository = mock(CardRepository.class);
        mockUserRepository = mock(UserRepository.class);

        authService = new AuthServiceImpl(mockUserRepository, mockAccountRepository, mockCardRepository);

        Class authServiceClass = authService.getClass();
        try {
            Field accountRepositoryField = authServiceClass.getDeclaredField("accountRepository");
            accountRepositoryField.setAccessible(true);
            accountRepositoryField.set(authService, mockAccountRepository);

            Field cardRepositoryField = authServiceClass.getDeclaredField("cardRepository");
            cardRepositoryField.setAccessible(true);
            cardRepositoryField.set(authService, mockCardRepository);

            Field userRepositoryField = authServiceClass.getDeclaredField("userRepository");
            userRepositoryField.setAccessible(true);
            userRepositoryField.set(authService, mockUserRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ifCardNumberInLoginIsEmpty() {
        String cardNumber = "";
        String pin = "";

        try {
            authService.login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("Error! Card number is empty", thrown.getMessage());
        }
    }

    @Test
    public void ifPinInLoginIsEmpty() {
        String cardNumber = "123456";
        String pin = "";

        try {
            authService.login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("Error! Pin is empty", thrown.getMessage());
        }
    }

    @Test
    public void ifCardInLoginIsIncorrect() {
        String cardNumber = "123456";
        String pin = "1234";

        when(mockCardRepository.getByNumber(Mockito.anyString())).thenReturn(Optional.empty());

        try {
            authService.login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("Error! Card number is incorrect", thrown.getMessage());
        }
    }

    @Test
    public void ifPinParameterInLoginIsIncorrect() {
        String cardNumber = "1234567890123456";
        String pin = "4321";

        when(mockCardRepository.getByNumber(Mockito.anyString())).thenReturn(Optional.of(getTestingCard()));

        try {
            authService.login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("Error! Pin code is incorrect", thrown.getMessage());
        }
    }

    @Test
    public void ifParametersInLoginIsCorrect() {
        String cardNumber = "1234567890123456";
        String pin = "1234";

        Card cardMock = getTestingCard();
        Account accountMock = getTestingAccount();
        User userMock = getTestingUser();

        when(mockCardRepository.getByNumber(Mockito.anyString())).thenReturn(Optional.of(cardMock));
        when(mockAccountRepository.getById(Mockito.anyLong())).thenReturn(accountMock);
        when(mockUserRepository.getById(Mockito.anyLong())).thenReturn(userMock);

        AuthDescriptor testAuthDescriptor = authService.login(cardNumber, pin);
        AuthDescriptor controlAuthDescriptor = new AuthDescriptor(userMock, accountMock, cardMock);

        Assertions.assertEquals(controlAuthDescriptor, testAuthDescriptor);
    }

    public Card getTestingCard() {
        return new Card(123456L, "1234567890123456", 54321, "1234", Card.Plan.BASE, ZonedDateTime.now(), false, 0);
    }

    public Account getTestingAccount() {
        return new Account(1L, "123", true, "plan", 10000D, 1L, null);
    }

    public User getTestingUser() {
        return new User(
            13245L,
            "Name",
            "Surname",
            "email@mail.com",
            "password",
            "phone number",
            "username",
            true,
            0
        );
    }

}
