package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.repository.CardRepository;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
            throw new Exception("Account not found");
        }
        Card card = new Card(account, "1234567890123", "1234", Card.Plan.valueOf(plan), LocalDateTime.now());
        return save(card);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }
}
