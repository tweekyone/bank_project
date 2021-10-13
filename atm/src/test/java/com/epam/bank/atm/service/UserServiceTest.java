package com.epam.bank.atm.service;

import static org.mockito.Mockito.mock;

import com.epam.bank.atm.BaseTest;
import com.epam.bank.atm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest extends BaseTest {

    @Test
    public void ifGetUserByIdArgumentIsEmpty() {
        UserRepository userRepository = mock(UserRepository.class);
        try {
            new UserServiceImpl(userRepository).getUserById(0);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("User ID is null!", thrown.getMessage());
        }
    }

    @Test
    public void ifUpdateArgumentIsEmpty() {
        UserRepository userRepository = mock(UserRepository.class);
        try {
            new UserServiceImpl(userRepository).update(null);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown) {
            Assertions.assertEquals("User is null!", thrown.getMessage());
        }
    }
}
