package com.epam.clientinterface.controller.dto.response;

import com.epam.clientinterface.entity.Card;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCardResponse {
    private short status;
    private String description;
    private String number;
    private Card.Plan plan;
    private LocalDateTime expirationDate;
}
