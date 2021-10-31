package com.epam.clientinterface.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.configuration.TestConfiguration;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import com.epam.clientinterface.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class,
    loader = AnnotationConfigContextLoader.class)
class AuthServiceImplTest {

    // Mocked Account factory (create)
    // mock(Account.Factory.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    private static final List<Account> accounts = new ArrayList<>();

    private static final User user =
        new User("Ivan", "Popov", "vanok@gmail.com", "1234",
            "+79100000", "roma_mock", accounts);

    private static final String email = "vanok@gmail.com";

    // private final Account mockedAccount = new Account(user, "40702810123456789125",
    //     true, Account.Plan.BASE, 5678.58);

    private static final User mockedUser = mock(User.class);

    @BeforeAll
    public static void setup() {
        // when(userService.create("Ivan", "Popov", "vanok@gmail.com", "1234",
        //     "+79100000", "roma_mock")).thenReturn(mockedUser);
    }

    @Test
    void signUpWithNewEmail() {
        // when(userService.create("Ivan", "Popov", "vanok@gmail.com", "1234",
        //     "+79100000", "roma_mock")).thenReturn(user);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        authService.signUp("Ivan", "Popov", "+79100000",
            "1234", email, "roma_mock");
        // verify(userService, atLeastOnce()).create(eq("email"), any(), any(), any(), any(), any());
        verify(userRepository, atLeastOnce()).existsByEmail(eq(email));
    }

    @Test
    void signUpWithAlreadyExistsEmail() {
        when(userRepository.existsByEmail(email)).thenReturn(true);
        //.thenThrow(new UserAlreadyExistException(email));

        Assertions.assertThrows(UserAlreadyExistException.class, () -> authService.signUp("Ivan",
            "Popov", "+79100000",
            "1234", email, "roma_mock"));

        verify(userRepository, atLeastOnce()).existsByEmail(eq(email));
    }
}