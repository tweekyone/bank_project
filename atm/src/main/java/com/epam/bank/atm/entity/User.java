package com.epam.bank.atm.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone_number;
    private Role role;

    public User(Long id) {
        this.id = id;
    }
    public enum Role {
        admin,
        client
    }
}
