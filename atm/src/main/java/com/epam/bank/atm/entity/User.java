package com.epam.bank.atm.entity;

import java.util.Objects;

public class User {
    private final long ID;
    private String name;
    private String surname;
    private String phoneNumber;
    private String username;
    private String email;
    private String password;

    public User(long ID, String name, String surname, String phoneNumber, String username, String email,
                String password) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getID() {
        return ID;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID && name.equals(user.name) && surname.equals(user.surname) && phoneNumber
            .equals(user.phoneNumber) && username.equals(user.username) && email.equals(user.email) && password
            .equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, surname, phoneNumber, username, email, password);
    }
}
