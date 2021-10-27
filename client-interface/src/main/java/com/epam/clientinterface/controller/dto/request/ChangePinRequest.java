package com.epam.clientinterface.controller.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePinRequest {
    @Positive
    private Long cardId;

    @NotNull
    private String oldPin;

    @NotNull
    private String newPin;
}
