package com.epam.clientinterface.controller.dto.request;

import com.epam.clientinterface.entity.CardPlan;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NewCardRequest {
    @NotNull
    private CardPlan plan;
}
