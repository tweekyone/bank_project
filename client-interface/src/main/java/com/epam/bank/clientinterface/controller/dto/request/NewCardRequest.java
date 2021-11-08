package com.epam.bank.clientinterface.controller.dto.request;

import com.epam.bank.clientinterface.entity.CardPlan;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NewCardRequest {
    @NotNull
    private CardPlan plan;
}
