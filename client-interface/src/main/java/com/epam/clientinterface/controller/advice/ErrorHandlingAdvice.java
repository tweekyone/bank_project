package com.epam.clientinterface.controller.advice;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import java.util.HashMap;
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

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistException exception,
                                                          WebRequest request) {
        return handleExceptionInternal(
            exception,
            new ErrorResponse("userAlreadyExists", HttpStatus.BAD_REQUEST),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }
}
