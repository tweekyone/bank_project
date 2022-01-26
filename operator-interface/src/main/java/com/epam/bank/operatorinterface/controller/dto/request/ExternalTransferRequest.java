package com.epam.bank.operatorinterface.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ExternalTransferRequest {

    @Positive
    private long sourceAccountId;

    @NotBlank
    private String destinationAccountNumber;

    @Positive
    private double amount;
}
