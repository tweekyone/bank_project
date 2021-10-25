package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.AccountIsNullException;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.exception.PlanNotEnumException;
import com.epam.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
import java.util.Random;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final AccountService accountService;

    public Card createCard(Long accountId, String plan) {

        Account account;
        try {
            account = accountService.findById(accountId);
        } catch (IllegalArgumentException e) {
            throw new AccountIsNullException();
        }

        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }

        Card card = new Card();
        Card.Plan cardPlan;
        String pinCode = generatePinCode();

        try {
            cardPlan = Card.Plan.valueOf(plan);
        } catch (IllegalArgumentException e) {
            throw new PlanNotEnumException(plan);
        }

        card.setAccount(account);
        card.setPinCode(pinCode);
        card.setPlan(cardPlan);
        card.setExpirationDate(LocalDateTime.now().plusYears(3));

        while (true) {
            try {
                String number = generateCardNumber();
                card.setNumber(number);
                return cardRepository.save(card);
            } catch (DataIntegrityViolationException e) {
                continue;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("CardEntity is null");
            }
        }
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
