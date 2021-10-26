package com.epam.clientinterface.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private final String type;
    private final short status;

    public ErrorResponse(String type, HttpStatus status) {
        this.type = type;
        this.status = (short)status.value();
    }
}
