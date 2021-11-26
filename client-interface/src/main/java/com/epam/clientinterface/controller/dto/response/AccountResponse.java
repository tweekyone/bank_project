package com.epam.clientinterface.controller.dto.response;

import com.epam.clientinterface.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountResponse {

    private final String number;

    private final boolean isDefault;

    private final Account.Plan plan;

    private final double amount;
}
