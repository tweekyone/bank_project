package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.service.util.NewPinValidator;
import java.time.ZonedDateTime;
import java.util.Random;
import javax.transaction.Transactional;
import javax.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public Card changePinCode(ChangePinRequest pinRequest) {
        Card card = cardRepository.findById(pinRequest.getCardId()).orElse(null);
        if (card == null) {
            throw new CardNotFoundException(pinRequest.getCardId());
        }

        NewPinValidator.validatePinCode(card, pinRequest);

        Integer pinCounter = card.getPinCounter();

        if (pinCounter < 3) {
            card.setPinCounter(pinCounter + 1);
            card.setPinCode(pinRequest.getNewPin());
            return cardRepository.save(card);
        } else {
            throw new ChangePinException();
        }
    }

    @Scheduled(cron = "${droppincounter.cron.expression}")
    public void dropPinCounter() {
        cardRepository.dropPinCounter();
    }

    public @NonNull Card releaseCard(@NonNull Long accountId, @NonNull CardPlan plan) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        DomainLogicChecker.assertAccountIsNotClosed(account);

        String pinCode = generatePinCode();
        String number;
        // TODO: a potentially infinite loop
        do {
            number = generateCardNumber();
        } while (cardRepository.findCardByNumber(number).isPresent());

        Card card = new Card(account, number, pinCode, plan, false, ZonedDateTime.now().plusYears(3));
        return cardRepository.save(card);
    }

    public @NonNull Card blockCard(@Positive Long cardId) {
        Card card = this.cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));

        DomainLogicChecker.assertAccountIsNotClosed(card.getAccount());

        card.setBlocked(true);
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
}
