package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    //temporary method
    @GetMapping(path = "/{number}")
    public @ResponseBody
    Card findByNumber(@PathVariable String number) {
        Card card = cardService.findByNumber(number);
        return card;
    }

    //temporary method
    @PostMapping(path = "/create")
    public ResponseEntity<Card> create(@Valid @RequestBody Card card) {
        Card newCard = cardService.save(card);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }
    //
    // @PatchMapping(path = "/{cardId}/change-password")
    // public ResponseEntity<?> changePassword(
    //     @Valid @RequestBody ChangePinRequest request,
    //     @PathVariable("cardId") String cardId) {
    //     cardService.changePinCode(Long.parseLong(cardId), request);
    //     return ResponseEntity.ok("Pin Code successfully changed!");
    // }

    //temporary method
    @GetMapping("/connected")
    public ResponseEntity<?> returnOk() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePinRequest request) {
        cardService.changePinCode(request);
        return ResponseEntity.ok("Pin Code successfully changed!");
    }
}
