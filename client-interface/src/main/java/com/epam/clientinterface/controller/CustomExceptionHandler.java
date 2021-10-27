package com.epam.clientinterface.controller;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.exception.AccountNotFoundException;
import java.util.HashMap;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        var body = new HashMap<>();
        body.put("type", "validation");
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("errors", ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                groupingBy(
                    FieldError::getField,
                    mapping(FieldError::getDefaultMessage, toList())
                )
            ));

        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Override
    public @NonNull ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        ErrorResponse body = new ErrorResponse("badRequest", HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<Object> handleAccountNotFound(Exception ex, WebRequest request) {
        ErrorResponse body = new ErrorResponse("accountNotFound", HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
