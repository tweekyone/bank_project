package com.epam.bank.atm.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone_number;
    private Role role;

    public enum Role {
        admin,
        client
    }
}
