package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;
import com.epam.bank.atm.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Test
    public void loginIfCardNumberIsEmpty(){
        String cardNumber = "";
        String pin = "";

        try {
            new AuthServiceImpl().login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("Error! Card number is empty", thrown.getMessage());
        }
    }

    @Test
    public void loginIfPinIsEmpty(){
        String cardNumber = "123456";
        String pin = "";

        try {
            new AuthServiceImpl().login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("Error! Pin is empty", thrown.getMessage());
        }
    }

    @Test
    public void loginIfCardIsEmpty(){
        String cardNumber = "132436";
        String pin = "1234";
        CardRepository cardRepository = mock(CardRepository.class);
        when(cardRepository.getById(anyLong())).thenReturn(null);
        //Card emptyCard = cardRepository.getById(Long.getLong(cardNumber));

        try {
            new AuthServiceImpl().login(cardNumber, pin);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("Error! Card number is incorrect", thrown.getMessage());
        }
    }

    @Test
    public void loginIfCardNumberAndPinIsCorrect(){
        String cardNumber = "132436";
        String pin = "1234";
        CardRepository cardRepository = mock(CardRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        UserRepository userRepository = mock(UserRepository.class);

        when(cardRepository.getById(Long.valueOf(cardNumber))).thenReturn(getTestingCard());
        Card testCard = cardRepository.getById(Long.getLong(cardNumber));
        when(accountRepository.getById(testCard.getAccountId())).thenReturn(getTestAccount());
        Account testAccount = accountRepository.getById(testCard.getAccountId());
        when(userRepository.getById(testAccount.getId())).thenReturn(getTestUser());
        User testUser = userRepository.getById(testAccount.getId());



    }

    public Card getTestingCard(){
        return new Card(1l, 123456, 54321, 1234);
    }

    public Account getTestAccount() {
        return new Account(65321, 7689);
    }

    public User getTestUser(){
        return new User(7689, "Name", "Surname", "8(911)123-32-13",
                "Username", "email@mail.com", "password");
    }
}
