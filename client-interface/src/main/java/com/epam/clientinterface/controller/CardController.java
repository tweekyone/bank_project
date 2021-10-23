package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.NewCardRequest;
import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.controller.dto.response.NewCardResponse;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.service.CardService;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping(path = "/account/{accountId}/cards", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public NewCardResponse releaseCard(@PathVariable Long accountId,  @RequestBody NewCardRequest request)
        throws Exception {
        Card card;
        NewCardResponse newCardResponse;
        String plan = request.getPlan();
        card = cardService.createCard(accountId, plan);
        newCardResponse = new NewCardResponse((short)200, "Card has been created.", card.getNumber(),
            card.getPlan(), card.getExpirationDate().format(DateTimeFormatter.ofPattern("eee, MMM dd. yyyy")));
        return newCardResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorResponse> handleIllegalArgumentExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Bad request", (short)400, ex.getClass().toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorResponse> handleNullPointerExceptionExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Service error", (short)500, ex.getClass().toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public final ResponseEntity<ErrorResponse> handleHttpMessageExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Bad request", (short)400, ex.getClass().toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Bad request", (short)400, ex.getClass().toString(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
