package com.epam.clientinterface.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private String error;

}
