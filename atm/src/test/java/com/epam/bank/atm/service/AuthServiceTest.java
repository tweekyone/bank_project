package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Test
    public void ifCardNumberIsEmpty(){
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
    public void ifPinIsEmpty(){
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
    public void ifCardIsEmpty(){
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

    public Card getTestingCard(){
        return new Card(1l, 123456, 54321, 1234);
    }
}
