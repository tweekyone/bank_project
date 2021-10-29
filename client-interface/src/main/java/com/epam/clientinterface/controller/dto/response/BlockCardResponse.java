package com.epam.clientinterface.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockCardResponse {
    private short status;
    private String description;
    private String number;

    public BlockCardResponse(HttpStatus status, String description, String number) {
        this.status = (short) status.value();
        this.description = description;
        this.number = number;
    }
}
