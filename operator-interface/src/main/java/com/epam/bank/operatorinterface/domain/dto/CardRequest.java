package com.epam.bank.operatorinterface.domain.dto;

import com.epam.bank.operatorinterface.enumerated.CardPlan;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardRequest {

    @Positive
    private long accountId;
    @NotNull
    private CardPlan plan;
}
