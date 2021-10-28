package com.epam.clientinterface.controller.dto.request;

import javax.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalTransferRequest {
    @Positive
    private long sourceAccountId;

    @Positive
    private long destinationAccountId;

    @Positive
    private double amount;
}
