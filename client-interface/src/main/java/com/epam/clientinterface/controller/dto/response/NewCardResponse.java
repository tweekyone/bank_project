package com.epam.clientinterface.controller.dto.response;

import com.epam.clientinterface.entity.Card;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewCardResponse {
    private final short status;
    private final String number;
    private final Card.Plan plan;
    private final String expirationDate;
}
