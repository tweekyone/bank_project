package com.epam.bank.clientinterface.controller.dto.request;

import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalTransferRequest {
    @Positive
    private long sourceAccountId;

    @Positive
    private long destinationAccountId;

    @Positive
    private double amount;
}
