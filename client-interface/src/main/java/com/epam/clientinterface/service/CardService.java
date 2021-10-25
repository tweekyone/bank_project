package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.CardEntity;
import com.epam.clientinterface.repository.CardRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    //temporary method
    public CardEntity save(CardEntity card) {
        return cardRepository.save(card);
    }

    //temporary method
    public CardEntity findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    public CardEntity changePassword(Long cardId, String rawPin) {
        CardEntity card = cardRepository.getById(cardId);
        card.setPin(rawPin);
        return cardRepository.save(card);
    }
}
