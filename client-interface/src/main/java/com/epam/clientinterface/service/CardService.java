package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.entity.PinCounter;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.PinCounterRepository;
import com.epam.clientinterface.service.util.NewPinValidator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final PinCounterRepository pinCounterRepository;
    private final AccountRepository accountRepository;

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

    public @NonNull Card releaseCard(@NonNull Long accountId, @NonNull CardPlan plan) {

        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        String pinCode = generatePinCode();
        String number;
        // TODO: a potentially infinite loop
        do {
            number = generateCardNumber();
        } while (cardRepository.findCardByNumber(number).isPresent());

        Card card = new Card(account.get(), number, pinCode, plan, LocalDateTime.now().plusYears(3),
            new PinCounter.AsFirstFactory(LocalDateTime.now()));
        return cardRepository.save(card);
    }

    protected String generateCardNumber() {
        return randomGenerateStringOfInt(16);
    }

    protected String generatePinCode() {
        return randomGenerateStringOfInt(4);
    }

    public String randomGenerateStringOfInt(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
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
