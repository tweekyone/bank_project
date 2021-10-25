package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.response.ErrorResponse;
import com.epam.clientinterface.exception.AccountIsNullException;
import com.epam.clientinterface.exception.AccountNotFoundException;
import com.epam.clientinterface.exception.PlanNotEnumException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse error = new ErrorResponse("Bad request", (short) HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);

    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        ErrorResponse error = new ErrorResponse("Invalid json", (short) HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlanNotEnumException.class)
    public final ResponseEntity<ErrorResponse> handlePlanNotExist(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Plan is not enum", (short) HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleAccountNotFound(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Account not found",(short) HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountIsNullException.class)
    public final ResponseEntity<ErrorResponse> handleAccountIsNull(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Account is null", (short) HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorResponse> handleNullPointer(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Service error", (short) HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorResponse> handleIllegalArgument(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Service error", (short) HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
