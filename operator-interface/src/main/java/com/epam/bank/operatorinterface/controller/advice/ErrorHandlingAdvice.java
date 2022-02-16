package com.epam.bank.operatorinterface.controller.advice;

import com.epam.bank.operatorinterface.controller.dto.response.ErrorResponse;
import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.exception.AccountCanNotBeClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.AccountNumberGenerationTriesLimitException;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import com.epam.bank.operatorinterface.exception.TransactionCsvImportException;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.exception.TransferException;
import com.epam.bank.operatorinterface.exception.UserNotFoundException;
import com.epam.bank.operatorinterface.exception.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
@Slf4j
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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put("type", "validation");
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("errors", ex.getViolations().stream()
            .map(v -> {
                var fieldError = new HashMap<>();
                fieldError.put("field", v.getPropertyPath().toString());
                fieldError.put("type", v.getConstraintDescriptor().getAnnotation().annotationType().getName());
                fieldError.put("error", v.getMessage());

                return fieldError;
            })
            .collect(Collectors.toList())
        );

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public @NonNull ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatus status,
        @NonNull WebRequest request
    ) {
        return handleExceptionInternal(ex, handleException(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
        Exception.class,
        AccountNumberGenerationTriesLimitException.class,
        TransactionCsvImportException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleInternalServerError(Exception e) {
        return handleException(e);
    }

    @ExceptionHandler({
        AccountNotFoundException.class,
        UserNotFoundException.class,
        CardNotFoundException.class,
        TransactionNotFoundException.class,
        NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFound(Exception e) {
        return handleException(e);
    }

    @ExceptionHandler({
        AccountIsClosedException.class,
        AccountCanNotBeClosedException.class,
        AccountIsNotSupposedForWithdrawException.class,
        AccountIsNotSupposedForExternalTransferException.class,
        CardIsBlockedException.class,
        InvalidPinCodeFormatException.class,
        NotEnoughMoneyException.class,
        TooManyPinCodeChangesPerDayException.class,
        TransferException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBadRequest(Exception e) {
        return handleException(e);
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ErrorResponse handleIoException(Exception e) {
        return handleException(e);
    }

    private ErrorResponse handleException(Exception e) {
        log.error(String.format("Catch exception %s", e.getMessage()), e);
        return new ErrorResponse(e.getClass().getName());
    }
}
