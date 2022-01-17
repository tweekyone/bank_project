package com.epam.bank.operatorinterface.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCardPinCodeRequest {
    @NotNull @NotBlank @Pattern(regexp = "[0-9]{4}")
    private String pinCode;
}
