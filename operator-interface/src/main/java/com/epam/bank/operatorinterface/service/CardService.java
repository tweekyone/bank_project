package com.epam.bank.operatorinterface.service;

import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public void changePinCode(long id, String pinCode) {
        var card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        assertCardIsNotBlocked(card);
        assertCardPinCodeFormatIsValid(pinCode);
        assertPinCodeCanBeChanged(card);

        card.changePinCode(pinCode);
        cardRepository.save(card);
    }

    private static void assertCardIsNotBlocked(Card card) {
        if (card.isBlocked()) {
            throw new CardIsBlockedException(card.getId());
        }
    }

    private static void assertCardPinCodeFormatIsValid(String pinCode) {
        if (!StringUtils.isNumeric(pinCode) || pinCode.length() != 4) {
            throw new InvalidPinCodeFormatException();
        }
    }

    private static void assertPinCodeCanBeChanged(Card card) {
        if (card.getPinCounter() >= 3) {
            throw new TooManyPinCodeChangesPerDayException(card.getId());
        }
    }
}
