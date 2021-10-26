package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
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
    private final AccountService accountService;

    public @NonNull Card createCard(@NonNull Long accountId, @NonNull Card.Plan plan) {

        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }

        String pinCode = generatePinCode();
        Card card = new Card();
        card.setAccount(account);
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
