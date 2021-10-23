package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
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

    public Card createCard(Long accountId, String plan) throws Exception {
        Account account = accountService.findById(accountId);
        if (account == null) {
            throw new Exception("Account not found.");
        }

        Card saveCard = new Card();
        while (true) {
            try {
                String number = generateCardNumber();
                String pinCode = generatePinCode();
                Card.Plan cardPlan = Card.Plan.valueOf(plan);
                Card card = new Card(account, number, pinCode, cardPlan, LocalDateTime.now());
                saveCard = save(card);
                break;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Plan is not enum.");
            } catch (DataIntegrityViolationException e) {
                continue;
            } catch (NullPointerException e) {
                throw new NullPointerException("Card has not been created.");
            }
        }
        return saveCard;
    }

    public Card save(Card card) {
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
