package com.epam.bank.operatorinterface.controller.dto.response;

import com.epam.bank.operatorinterface.entity.AccountPlan;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountResponse {
    private long id;
    private String number;
    private boolean isDefault;
    private String plan;
    private double amount;
    private long userId;
    private List<CardResponse> cards;
    private LocalDateTime closedAt;
}
