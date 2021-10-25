package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.NewCardRequest;
import com.epam.clientinterface.controller.dto.response.NewCardResponse;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/cards", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public NewCardResponse releaseCard(@PathVariable Long accountId, @RequestBody NewCardRequest request) {
        // TODO: authentication
        String plan = request.getPlan();
        Card card;
        NewCardResponse newCardResponse;

        card = cardService.createCard(accountId, plan);
        newCardResponse = new NewCardResponse((short)200, "Card has been created", card.getNumber(),
            card.getPlan(), card.getExpirationDate().format(DateTimeFormatter.ofPattern("eee, MMM dd. yyyy")));
        return newCardResponse;
    }
}
