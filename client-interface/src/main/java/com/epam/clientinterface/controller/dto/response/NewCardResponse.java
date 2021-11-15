package com.epam.clientinterface.controller.dto.response;

import com.epam.clientinterface.entity.CardPlan;
import java.time.LocalDateTime;
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
    private LocalDateTime expirationDate;

    public NewCardResponse(HttpStatus status, String description, String number, CardPlan plan,
                           LocalDateTime expirationDate) {
        this.status = status.value();
        this.description = description;
        this.number = number;
        this.plan = plan;
        this.expirationDate = expirationDate;
    }
}
