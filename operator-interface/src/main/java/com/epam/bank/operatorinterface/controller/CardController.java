package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.ChangeCardPinCodeRequest;
import com.epam.bank.operatorinterface.domain.dto.CardRequest;
import com.epam.bank.operatorinterface.domain.dto.CardResponse;
import com.epam.bank.operatorinterface.service.CardService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse createCard(@RequestBody @Valid CardRequest cardRequest) {
        return cardService.createCard(cardRequest);
    }

    @PutMapping("/{cardId}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockCard(@PathVariable long cardId) {
        cardService.blockCard(cardId);
    }

    @PutMapping("/{cardId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeCard(@PathVariable long cardId) {
        cardService.closeCard(cardId);
    }

    @PatchMapping("/{id}/pin-code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePinCode(
        @PathVariable long id, @RequestBody @Valid ChangeCardPinCodeRequest request) {
        cardService.changePinCode(id, request.getPinCode());
    }
}
