package com.epam.bank.operatorinterface.controller.dto.request;

import com.epam.bank.operatorinterface.configuration.util.InternalRequestConstrain;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
@InternalRequestConstrain
public class InternalTransferRequest {

    @Positive
    private long sourceAccountId;

    private String destinationAccountNumber;

    private String destinationCardNumber;

    @Positive
    private double amount;
}
