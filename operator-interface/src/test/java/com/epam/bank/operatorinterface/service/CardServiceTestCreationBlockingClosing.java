package com.epam.bank.operatorinterface.service;

import static com.epam.bank.operatorinterface.util.TestDataFactory.getAccount;
import static com.epam.bank.operatorinterface.util.TestDataFactory.getCard;
import static com.epam.bank.operatorinterface.util.TestDataFactory.getCardRequest;
import static com.epam.bank.operatorinterface.util.TestDataFactory.getClosedAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.bank.operatorinterface.domain.dto.CardDto;
import com.epam.bank.operatorinterface.domain.dto.CardRequest;
import com.epam.bank.operatorinterface.domain.dto.CardResponse;
import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.entity.CardPlan;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.CardRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTestCreationBlockingClosing {

    @Mock
    CardRepository cardRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    CardService cardService;

    @Test
    void createCardSuccess() {
        Card card = getCard(54);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(getAccount(54)));
        when(cardRepository.save(any())).thenReturn(card);
        CardRequest request = getCardRequest(16);
        CardResponse response = cardService.createCard(request); //id, cardnumber, date
        assertTrue(response.getExpirationDate().isAfter(LocalDate.now()));
        assertEquals(16, response.getCardNumber().length());
        LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(response.getCardNumber());
    }

    @Test
    void closeCardSuccess() {
        Card card = getCard(1);
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(cardRepository.save(any())).thenReturn(card);
        CardDto cardDto = cardService.closeCard(card.getId());
        assertTrue(cardDto.isBlocked());
        assertTrue(cardDto.getExpirationDate().toLocalDate().isEqual(LocalDate.now()));
    }

    @Test
    void blockCardSuccess() {
        Card card = getCard(1);
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(cardRepository.save(any())).thenReturn(card);
        CardDto cardDto = cardService.blockCard(card.getId());
        assertTrue(cardDto.isBlocked());
    }

    @Test
    void shouldThrowNotFoundEx_IfAccountDoesNotExist() {
        assertThrows(NotFoundException.class, () -> cardService.createCard(new CardRequest(anyLong(), CardPlan.BASE)));
    }

    @Test
    void shouldThrowNotFoundEx_IfCardDoesNotExist() {
        assertThrows(NotFoundException.class, () -> cardService.blockCard(anyLong()));
        assertThrows(NotFoundException.class, () -> cardService.closeCard(anyLong()));
    }

    @Test
    void shouldThrowIllegalArgEx_IfAccountIsClosed() {
        Account closedAcc = getClosedAccount(1);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(closedAcc));
        assertThrows(IllegalArgumentException.class, () -> cardService.createCard(new CardRequest(closedAcc.getId(),
            CardPlan.BASE)));
    }
}
