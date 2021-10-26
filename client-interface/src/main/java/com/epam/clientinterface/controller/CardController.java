package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.NewCardRequest;
import com.epam.clientinterface.controller.dto.response.NewCardResponse;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.exception.PlanNotEnumException;
import com.epam.clientinterface.service.CardService;
import java.time.format.DateTimeFormatter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/cards")
    public NewCardResponse releaseCard(@PathVariable Long accountId,@Valid @RequestBody NewCardRequest request) {
        // TODO: authentication

        Card card = cardService.createCard(accountId, request.getPlan());
        return new NewCardResponse((short)200, "Card has been created", card.getNumber(),
            card.getPlan(), card.getExpirationDate().format(DateTimeFormatter.ofPattern("eee, MMM dd. yyyy")));
    }
}
