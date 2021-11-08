package com.epam.bank.clientinterface.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalTransferRequest {
    @Positive
    private long sourceAccountId;

    @NotBlank
    private String destinationAccountNumber;

    @Positive
    private double amount;
}
