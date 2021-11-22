package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
import java.util.Random;
import javax.transaction.Transactional;
import javax.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public @NonNull Card releaseCard(@NonNull Long accountId, @NonNull CardPlan plan) {
        Account account = accountRepository.findById(accountId).orElseThrow(
            () -> new AccountNotFoundException(accountId)
        );

        if (account.isClosed()) {
            throw new AccountIsClosedException(accountId);
        }

        String pinCode = generatePinCode();
        String number;
        // TODO: a potentially infinite loop
        do {
            number = generateCardNumber();
        } while (cardRepository.findCardByNumber(number).isPresent());

        Card card = new Card(account, number, pinCode, plan, false, LocalDateTime.now().plusYears(3));
        return cardRepository.save(card);
    }

    public @NonNull Card blockCard(@Positive Long cardId) {
        Card card = this.cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));

        if (card.getAccount().isClosed()) {
            throw new AccountIsClosedException(card.getAccount().getId());
        }

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
