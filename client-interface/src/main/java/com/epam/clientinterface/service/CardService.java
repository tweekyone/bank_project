package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.entity.Card;
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
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    //temporary method
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    public void changePinCode(ChangePinRequest pinRequest) {
        Card card = cardRepository.getById(pinRequest.getCardId());
        card.setPinCode(pinRequest.getNewPin());
        save(card);
        System.out.println();
    }
}
