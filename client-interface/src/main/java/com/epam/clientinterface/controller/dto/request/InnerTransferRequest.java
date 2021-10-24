package com.epam.clientinterface.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InnerTransferRequest {
    private long sourceAccountId;
    private long destinationAccountId;
    private double amount;
}
