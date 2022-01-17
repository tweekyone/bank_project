package com.epam.bank.operatorinterface.controller.dto.response;

import com.epam.bank.operatorinterface.entity.CardPlan;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CardResponse {
    private long id;
    private String number;
    private long accountId;
    private String plan;
    private boolean isBlocked;
    private ZonedDateTime expirationDate;
}
