package com.epam.clientinterface.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final String name;

    private final String surname;

    private final String phoneNumber;

    private final String username;

    private final String email;

    private final AccountResponse accountsResponse;
}
