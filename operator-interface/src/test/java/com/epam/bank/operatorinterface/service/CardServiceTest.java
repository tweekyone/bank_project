package com.epam.bank.operatorinterface.service;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.repository.CardRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepositoryMock;

    @Captor
    private ArgumentCaptor<Card> cardCaptor;

    @InjectMocks
    private CardService cardService;

    @Test
    public void shouldChangePinCode() {
        var cardFixture = TestDataFactory.getCard();
        final var pinCounterBefore = cardFixture.getPinCounter();
        final var pinCode = RandomStringUtils.randomNumeric(4);

        when(cardRepositoryMock.findById(anyLong())).thenReturn(Optional.of(cardFixture));
        when(cardRepositoryMock.save(cardFixture)).then(invocation -> {
            cardFixture.onSave();
            return cardFixture;
        });

        cardService.changePinCode(cardFixture.getId(), pinCode);

        verify(cardRepositoryMock).save(cardCaptor.capture());
        Assertions.assertThat(cardCaptor.getValue().getId()).isEqualTo(cardFixture.getId());
        Assertions.assertThat(cardCaptor.getValue().getPinCode()).isEqualTo(pinCode);
        Assertions.assertThat(cardCaptor.getValue().getPinCounter()).isEqualTo(pinCounterBefore + 1);
    }

    @Test
    public void shouldThrowIfCanNotFindCard() {
        var cardId = RandomUtils.nextLong();
        var pinCode = RandomStringUtils.randomNumeric(4);

        when(cardRepositoryMock.findById(cardId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> cardService.changePinCode(cardId, pinCode))
            .isExactlyInstanceOf(CardNotFoundException.class);
    }

    @Test
    public void shouldThrowIfTryToChangePinCodeOfBlockedCard() {
        var cardFixture = TestDataFactory.getBlockedCard();
        var pinCode = RandomStringUtils.randomNumeric(4);

        when(cardRepositoryMock.findById(cardFixture.getId())).thenReturn(Optional.of(cardFixture));

        Assertions.assertThatThrownBy(() -> cardService.changePinCode(cardFixture.getId(), pinCode))
            .isExactlyInstanceOf(CardIsBlockedException.class);
    }

    @Test
    public void shouldThrowIfNewPinCodeConsistsOfNonNumericSymbols() {
        var cardFixture = TestDataFactory.getCard();
        var pinCode = RandomStringUtils.randomAlphabetic(4);

        when(cardRepositoryMock.findById(cardFixture.getId())).thenReturn(Optional.of(cardFixture));

        Assertions.assertThatThrownBy(() -> cardService.changePinCode(cardFixture.getId(), pinCode))
            .isExactlyInstanceOf(InvalidPinCodeFormatException.class);
    }

    @Test
    public void shouldThrowIfNewPinCodeIsNumericButHasWrongLength() {
        var cardFixture = TestDataFactory.getCard();
        var pinCode = RandomStringUtils.randomNumeric(5);

        when(cardRepositoryMock.findById(cardFixture.getId())).thenReturn(Optional.of(cardFixture));

        Assertions.assertThatThrownBy(() -> cardService.changePinCode(cardFixture.getId(), pinCode))
            .isExactlyInstanceOf(InvalidPinCodeFormatException.class);
    }

    @Test
    public void shouldThrowIfTryToChangePinCodeMoreTimesIfItIsAllowed() {
        var cardFixture = TestDataFactory.getCardWithMaxPinCounter();
        var pinCode = RandomStringUtils.randomNumeric(4);

        when(cardRepositoryMock.findById(cardFixture.getId())).thenReturn(Optional.of(cardFixture));

        Assertions.assertThatThrownBy(() -> cardService.changePinCode(cardFixture.getId(), pinCode))
            .isExactlyInstanceOf(TooManyPinCodeChangesPerDayException.class);
    }
}
