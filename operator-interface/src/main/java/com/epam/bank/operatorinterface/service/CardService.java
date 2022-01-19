package com.epam.bank.operatorinterface.service;

import static com.epam.bank.operatorinterface.service.CardUtil.generateCardNumber;
import static com.epam.bank.operatorinterface.service.CardUtil.generatePinCode;

import com.epam.bank.operatorinterface.domain.dto.CardDto;
import com.epam.bank.operatorinterface.domain.dto.CardRequest;
import com.epam.bank.operatorinterface.domain.dto.CardResponse;
import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.CardRepository;
import java.time.ZonedDateTime;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public CardResponse createCard(CardRequest request) {
        Account acc = accountRepository.findById(request.getAccountId())
            .orElseThrow(() -> new NotFoundException(Account.class, request.getAccountId()));

        if (acc.isClosed()) {
            throw new IllegalArgumentException(String.format("Account %d is closed", request.getAccountId()));
        }

        Card card = new Card(generateCardNumber(), generatePinCode(), request.getPlan(), false,
            ZonedDateTime.now().plusYears(3), acc);
        card = cardRepository.save(card);
        return new CardResponse(card.getId(), card.getNumber(), card.getExpirationDate().toLocalDate());
    }

    public CardDto closeCard(long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException(Card.class, cardId));

        if (card.isClosed()) {
            return new CardDto(card);
        }
        card.setBlocked(true);
        card.setExpirationDate(ZonedDateTime.now());
        return new CardDto(cardRepository.save(card));
    }

    public CardDto blockCard(long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException(Card.class, cardId));
        if (card.isBlocked()) {
            return new CardDto(card);
        }
        card.setBlocked(true);
        return new CardDto(cardRepository.save(card));
    }

    public void changePinCode(long id, String pinCode) {
        var card = cardRepository.findById(id).orElseThrow(() -> new NotFoundException(Card.class, id));

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
