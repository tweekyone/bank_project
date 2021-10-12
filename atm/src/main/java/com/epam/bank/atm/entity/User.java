package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && name.equals(user.name) && surname.equals(user.surname) && username.equals(
            user.username) && email.equals(user.email) && password.equals(user.password) && phone_number.equals(
            user.phone_number) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, username, email, password, phone_number, role);
    }
}
