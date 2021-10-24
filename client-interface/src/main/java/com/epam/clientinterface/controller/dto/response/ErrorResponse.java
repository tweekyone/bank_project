package com.epam.clientinterface.controller.dto.response;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final String type;
    private final short status;
    private final String title;
    private final String detail;

    public ErrorResponse(
        @NonNull String type,
        @NonNull HttpStatus status,
        @NonNull String title,
        @NonNull String detail
    ) {
        this.type = type;
        this.status = (short) status.value();
        this.title = title;
        this.detail = detail;
    }
}
