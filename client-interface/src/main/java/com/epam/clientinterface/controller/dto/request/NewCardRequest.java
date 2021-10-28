package com.epam.clientinterface.controller.dto.request;

import com.epam.clientinterface.entity.Card;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCardRequest {
    @NotNull
    private Card.Plan plan;
}
