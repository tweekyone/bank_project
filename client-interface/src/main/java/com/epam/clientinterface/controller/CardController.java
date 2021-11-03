package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.NewCardRequest;
import com.epam.clientinterface.controller.dto.response.BlockCardResponse;
import com.epam.clientinterface.controller.dto.response.NewCardResponse;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/releaseCard")
    public ResponseEntity<NewCardResponse> releaseCard(@PathVariable("accountId") @Positive Long accountId,
                                                       @Valid @RequestBody NewCardRequest request) {
        Card card = cardService.releaseCard(accountId, request.getPlan());
        NewCardResponse response = new NewCardResponse(HttpStatus.CREATED, "Card has been created", card.getNumber(),
            card.getPlan(), card.getExpirationDate());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/card/{cardId}/block")
    public ResponseEntity<?> blockCard(@PathVariable @Positive Long cardId) {
        Card card = cardService.blockCard(cardId);
        BlockCardResponse response = new BlockCardResponse(HttpStatus.OK, "Card has been blocked",
            card.getNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

