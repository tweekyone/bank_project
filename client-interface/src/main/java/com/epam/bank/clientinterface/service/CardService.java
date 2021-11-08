package com.epam.bank.clientinterface.service;

import com.epam.bank.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.bank.clientinterface.entity.Account;
import com.epam.bank.clientinterface.entity.Card;
import com.epam.bank.clientinterface.entity.CardPlan;
import com.epam.bank.clientinterface.repository.AccountRepository;
import com.epam.bank.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

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

        Card card = new Card(account.get(), number, pinCode, plan, LocalDateTime.now().plusYears(3));
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
