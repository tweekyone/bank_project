package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.NewCardRequest;
import com.epam.clientinterface.controller.dto.response.NewCardResponse;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/cards", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public NewCardResponse releaseCard(@PathVariable Long accountId, @RequestBody NewCardRequest request) {
        String plan = request.getPlan();
        Card card;
        try {
            card = cardService.createCard(accountId, plan);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return new NewCardResponse((short) 200, card.getNumber(), card.getPlan(),
            card.getExpirationDate().format(DateTimeFormatter.ofPattern("eee, MMM dd. yyyy")));
    }
}
