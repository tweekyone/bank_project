package com.epam.bank.atm.entity;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;

    public User(Long id) {
        this.id = id;
    }

    public enum Role {
        admin,
        client
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id.equals(user.id)
            && name.equals(user.name)
            && surname.equals(user.surname)
            && email.equals(user.email)
            && password.equals(user.password)
            && phoneNumber.equals(user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, phoneNumber);
    }
}
