package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    static User testUser;
    static UserRepository mockUserRepository;

    @BeforeAll
    public static void init(){
        testUser = new User(
                        1234,
                        "User",
                        "Surname",
                        "8(911)123-32-12",
                        "Username",
                        "email@mail.com",
                        "password");
        mockUserRepository = mock(UserRepository.class);
    }


    @Test
    public void ifGetUserByIdArgumentIsEmpty(){
        UserRepository userRepository = mock(UserRepository.class);
        try {
            new UserServiceImpl(userRepository).getUserById(0);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("User ID is null!", thrown.getMessage());
        }
    }

    @Test
    public void ifUpdateArgumentIsEmpty(){
        try {
            new UserServiceImpl(mockUserRepository).update(null);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("User is null!", thrown.getMessage());
        }
    }

    @Test
    public void ifUpdateUserIsCorrect(){
        doNothing().when(mockUserRepository).save(isA(User.class));

        new UserServiceImpl(mockUserRepository).update(testUser);

        verify(mockUserRepository, times(1)).save(testUser);
    }
}
