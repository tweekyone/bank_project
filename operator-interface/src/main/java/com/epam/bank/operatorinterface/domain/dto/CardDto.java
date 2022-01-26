package com.epam.bank.operatorinterface.domain.dto;

import com.epam.bank.operatorinterface.entity.Card;
import com.epam.bank.operatorinterface.enumerated.CardPlan;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDto {

    private long id;
    private String number;
    private CardPlan plan;
    private boolean isBlocked;
    private ZonedDateTime expirationDate;

    public CardDto(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
        this.plan = card.getPlan();
        this.isBlocked = card.isBlocked();
        this.expirationDate = card.getExpirationDate();
    }
}
