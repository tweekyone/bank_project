package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.PinCounter;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.PinCounterRepository;
import com.epam.clientinterface.service.util.NewPinValidator;
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

        NewPinValidator.validatePinCode(card, pinRequest);

        PinCounter pinCounter = pinCounterRepository.findByCardId(card.getId());
        if (isLastChangingDateToday(pinCounter) && pinCounter.getChangeCount() < 3) {
            card.setPinCode(pinRequest.getNewPin());
            pinCounter.setLastChangingDate(LocalDateTime.now());
            pinCounter.setChangeCount(pinCounter.getChangeCount() + 1);
            pinCounterRepository.save(pinCounter);
            return cardRepository.save(card);
        } else if (isLastChangingDateToday(pinCounter) && pinCounter.getChangeCount() >= 3) {
            throw new ChangePinException();
        } else {
            card.setPinCode(pinRequest.getNewPin());
            pinCounter.setLastChangingDate(LocalDateTime.now());
            pinCounter.setChangeCount(1);
            pinCounterRepository.save(pinCounter);
            return cardRepository.save(card);
        }
    }

    private boolean isLastChangingDateToday(PinCounter pinCounter) {
        if (pinCounter.getLastChangingDate().getYear() == LocalDateTime.now().getYear()
            && pinCounter.getLastChangingDate().getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
            return true;
        } else {
            return false;
        }
    }
}
