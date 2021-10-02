package com.epam.bank.atm.entity;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter

public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone_number;

    public User(long id) {
        this.id = id;
    }
}
