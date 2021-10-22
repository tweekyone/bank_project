package com.epam.clientinterface.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private final String type;
    private final short status;
    private final String title;
    private final String detail;
}
