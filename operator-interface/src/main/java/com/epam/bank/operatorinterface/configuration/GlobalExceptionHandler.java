package com.epam.bank.operatorinterface.configuration;

import com.epam.bank.operatorinterface.domain.exceptions.NotFoundException;
import com.epam.bank.operatorinterface.exception.CardIsBlockedException;
import com.epam.bank.operatorinterface.exception.InvalidPinCodeFormatException;
import com.epam.bank.operatorinterface.exception.TooManyPinCodeChangesPerDayException;
import javax.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    CallError handleNotFoundExceptions(RuntimeException exception) {
        return handleException(exception);
    }

    @ExceptionHandler({
        CardIsBlockedException.class,
        InvalidPinCodeFormatException.class,
        TooManyPinCodeChangesPerDayException.class,
        ValidationException.class,
        MissingPathVariableException.class,
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    CallError handleBadRequestExceptions(RuntimeException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    CallError handleInternalServerError(Exception exception) {
        return handleException(exception);
    }

    private CallError handleException(Exception exception) {
        log.error(String.format("Catch exception %s", exception.getMessage()), exception);
        return new CallError(exception.getMessage(), exception.getClass().getSimpleName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CallError {
        private String message;
        private String className;
    }
}
