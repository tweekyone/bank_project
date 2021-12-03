package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String username;
    private boolean enabled = true;
    private int failedLoginAttempts = 0;

    public User(Long id) {
        this.id = id;
    }

    public enum Role {
        admin,
        client
    }
}
