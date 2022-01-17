package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.ChangeCardPinCodeRequest;
import com.epam.bank.operatorinterface.service.CardService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    @PatchMapping("/{id}/pin-code")
    public ResponseEntity<?> changePinCode(
            @PathVariable long id, @RequestBody @Valid ChangeCardPinCodeRequest request) {
        cardService.changePinCode(id, request.getPinCode());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
