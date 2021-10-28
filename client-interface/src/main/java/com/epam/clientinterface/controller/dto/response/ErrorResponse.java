package com.epam.clientinterface.controller.dto.response;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final String type;
    private final short status;

    public ErrorResponse(@NonNull String type, @NonNull HttpStatus status) {
        this.type = type;
        this.status = (short) status.value();
    }
}