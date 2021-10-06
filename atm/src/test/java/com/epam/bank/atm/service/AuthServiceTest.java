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
        CardRepository cardRepository = mock(CardRepository.class);
        Card card = getTestingCard();
        when(cardRepository.getById(card.getAccountId())).thenReturn(null);

        try {
            new AuthServiceImpl().login("123456", "1234");
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException thrown){
            Assertions.assertEquals("Error! Card number is incorrect", thrown.getMessage());
        }


    }

    public Card getTestingCard(){
        return new Card(1l, 123456, 54321, 1234);
    }
}
