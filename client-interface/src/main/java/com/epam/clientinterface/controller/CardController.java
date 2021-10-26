package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.entity.CardEntity;
import com.epam.clientinterface.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    //temporary method
    @GetMapping(path = "/{number}")
    public ResponseEntity<String> findByNumber(@PathVariable String number) throws JsonProcessingException {
        String cardJson = new ObjectMapper().writeValueAsString(cardService.findByNumber(number));
        return new ResponseEntity<>(cardJson, HttpStatus.OK);
    }

    //temporary method
    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody CardEntity card) throws JsonProcessingException {
        String cardJson = new ObjectMapper().writeValueAsString(cardService.save(card));
        return new ResponseEntity<>(cardJson, HttpStatus.OK);
    }

    @PatchMapping(path = "/{cardId}/change-password")
    public ResponseEntity<String> changePassword(
        @RequestBody ChangePinRequest request,
        @PathVariable("cardId") String cardId) throws JsonProcessingException {
        CardEntity card = cardService.changePassword(Long.parseLong(cardId), request.getNewPin());
        String cardJson = new ObjectMapper().writeValueAsString(cardService.changePassword(card.getId(),
            request.getNewPin()));
        return new ResponseEntity<>(cardJson, HttpStatus.OK);
    }
}
