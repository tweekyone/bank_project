package com.epam.clientinterface.controller.advice;

import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import java.util.HashMap;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        HashMap body = new HashMap<>();
        body.put("type", "validation");
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("errors", ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(v -> {
                var fieldError = new HashMap<>();
                fieldError.put("field", v.getField());
                fieldError.put("type", v.getCode());
                fieldError.put("error", v.getDefaultMessage());

                return fieldError;
            })
            .toArray()
        );
        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getLocalizedMessage(),
            "Bad Request");
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({IncorrectPinException.class, ChangePinException.class, CardNotFoundException.class})
    public ResponseEntity<Object> handleChangePinException(Exception ex, WebRequest request) {
        String errorMessage;

        if (ex instanceof IncorrectPinException) {
            errorMessage = "Pin code is not valid";
        } else if (ex instanceof ChangePinException) {
            errorMessage = "Limit of attempts";
        } else {
            errorMessage = "Card not found";
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getLocalizedMessage(),
            errorMessage);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
