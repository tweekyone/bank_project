package com.epam.bank.operatorinterface.controller.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class ErrorResponse {
    private final String type;

    public ErrorResponse(@NonNull String type) {
        this.type = type;
    }
}
