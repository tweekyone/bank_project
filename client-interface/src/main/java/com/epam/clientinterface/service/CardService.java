package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
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

    public @NonNull Card createCard(@NonNull Long accountId, @NonNull Card.Plan plan) {

        Optional<Account> account = this.accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        String pinCode = generatePinCode();
        Card card = new Card();
        card.setAccount(account.get());
        card.setPinCode(pinCode);
        card.setPlan(plan);
        card.setExpirationDate(LocalDateTime.now().plusYears(3));

        String number;
        do {
            number = generateCardNumber();
        } while (cardRepository.findCardByNumber(number).isPresent());

        card.setNumber(number);
        return cardRepository.save(card);
    }

    protected String generateCardNumber() {
        return generate(16);
    }

    protected String generatePinCode() {
        return generate(4);
    }

    protected String generate(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }

}
