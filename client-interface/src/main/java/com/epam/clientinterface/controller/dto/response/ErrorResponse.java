package com.epam.clientinterface.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String type;
    private final HttpStatus status;
    private final String title;
    private final String detail;
}
