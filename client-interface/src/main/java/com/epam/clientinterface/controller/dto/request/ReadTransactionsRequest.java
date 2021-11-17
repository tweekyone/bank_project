package com.epam.clientinterface.controller.dto.request;

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
public class ReadTransactionsRequest {
    @Positive
    Long userId;

    @NotNull
    String accountNumber;
}
