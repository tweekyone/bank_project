package com.epam.bank.operatorinterface.controller.dto.response;

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
public class UserResponse {
    private long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String username;
    private String email;
    private List<AccountResponse> accounts;
    private boolean enabled;
    private int failedLoginAttempts;
}
