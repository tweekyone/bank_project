package com.epam.clientinterface.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePinRequest {
    private Long cardId;
    private String oldPin;
    private String newPin;
}
