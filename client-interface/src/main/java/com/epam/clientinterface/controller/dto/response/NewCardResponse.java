package com.epam.clientinterface.controller.dto.response;

import com.epam.clientinterface.entity.CardPlan;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewCardResponse {
    private int status;
    private String description;
    private String number;
    private CardPlan plan;
    private String expirationDate;

    public NewCardResponse(HttpStatus status, String description, String number, CardPlan plan,
                           ZonedDateTime expirationDate) {
        this.status = status.value();
        this.description = description;
        this.number = number;
        this.plan = plan;
        this.expirationDate = DateTimeFormatter.ISO_DATE_TIME.format(expirationDate);
    }
}
