package com.epam.bank.operatorinterface.controller.dto.request;

import com.epam.bank.operatorinterface.enumerated.AccountPlan;
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
public class CreateAccountRequest {
    @Positive
    private long userId;

    @NotNull
    private AccountPlan plan;
}
