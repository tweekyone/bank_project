package com.epam.clientinterface.controller;

import com.epam.clientinterface.configuration.security.SecurityUtil;
import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/releaseCard")
    public ResponseEntity<NewCardResponse> releaseCard(@PathVariable("accountId") @Positive Long accountId,
                                                       @Valid @RequestBody NewCardRequest request) {
        long userId = SecurityUtil.authUserId();
        Card card = cardService.releaseCard(accountId, request.getPlan(), userId);
        NewCardResponse response = new NewCardResponse(HttpStatus.CREATED, "Card has been created", card.getNumber(),
            card.getPlan(), card.getExpirationDate());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/card/{cardId}/blockCard")
    public ResponseEntity<?> blockCard(@PathVariable @Positive Long cardId) {
        long userId = SecurityUtil.authUserId();
        Card card = cardService.blockCard(cardId, userId);
        BlockCardResponse response = new BlockCardResponse(HttpStatus.OK, "Card has been blocked",
            card.getNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/cards/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePinRequest request) {
        cardService.changePinCode(request);
        return ResponseEntity.ok("Pin code successfully changed!");
    }
}