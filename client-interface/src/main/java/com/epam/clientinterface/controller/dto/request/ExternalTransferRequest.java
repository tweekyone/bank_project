package com.epam.clientinterface.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalTransferRequest {
    @Positive
    private long sourceAccountId;

    @NotNull
    @NotBlank
    private String destinationAccountNumber;

    @Positive
    private double amount;
}
