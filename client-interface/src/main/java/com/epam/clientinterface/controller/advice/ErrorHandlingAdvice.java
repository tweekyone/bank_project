package com.epam.clientinterface.controller.advice;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlingAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        var errors = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                groupingBy(
                    FieldError::getField,
                    mapping(FieldError::getDefaultMessage, toList())
                )
            );

        return handleExceptionInternal(ex, errors, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException exception, WebRequest request) {
        return handleExceptionInternal(
            exception,
            new ErrorResponse("accountNotFound", HttpStatus.NOT_FOUND, "Account not found", "Account not found"),
            new HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request
        );
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<Object> handleNotEnoughMoney(NotEnoughMoneyException exception, WebRequest request) {
        return handleExceptionInternal(
            exception,
            new ErrorResponse(
                "notEnoughMoney",
                HttpStatus.BAD_REQUEST,
                "Not enough money",
                "Account does not have enough money"
            ),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }
}
