package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.PinCounter;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.PinCounterRepository;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final PinCounterRepository pinCounterRepository;

    //temporary method
    public Card getById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    public Card changePinCode(ChangePinRequest pinRequest) {
        Card card = cardRepository.findById(pinRequest.getCardId()).orElse(null);
        if (card == null) {
            throw new CardNotFoundException(pinRequest.getCardId());
        }

        if (!card.getPinCode().equals(pinRequest.getOldPin())) {
            throw new IncorrectPinException("Old pin is incorrect");
        } else if (pinRequest.getNewPin().equals(pinRequest.getOldPin())) {
            throw new IncorrectPinException("New pin must be different");
        } else if (!pinRequest.getNewPin().matches("[0-9]+")) {
            throw new IncorrectPinException("Pin must contains numbers only");
        } else if (pinRequest.getNewPin().length() != 4) {
            throw new IncorrectPinException("Pin must contains 4 numbers only");
        }

        PinCounter pinCounter = pinCounterRepository.findByCardId(card.getId());
        if (pinCounter.getLastChangingDate().getYear() == LocalDateTime.now().getYear()
            && pinCounter.getLastChangingDate().getDayOfYear() == LocalDateTime.now().getDayOfYear()
            && pinCounter.getChangeCount() < 3) {
            card.setPinCode(pinRequest.getNewPin());
            pinCounter.setLastChangingDate(LocalDateTime.now());
            pinCounter.setChangeCount(pinCounter.getChangeCount() + 1);
            pinCounterRepository.save(pinCounter);
            return cardRepository.save(card);
        } else if (pinCounter.getLastChangingDate().getYear() == LocalDateTime.now().getYear()
            && pinCounter.getLastChangingDate().getDayOfYear() == LocalDateTime.now().getDayOfYear()
            && pinCounter.getChangeCount() >= 3) {
            throw new ChangePinException();
        } else {
            card.setPinCode(pinRequest.getNewPin());
            pinCounter.setLastChangingDate(LocalDateTime.now());
            pinCounter.setChangeCount(1);
            pinCounterRepository.save(pinCounter);
            return cardRepository.save(card);
        }
    }
}
