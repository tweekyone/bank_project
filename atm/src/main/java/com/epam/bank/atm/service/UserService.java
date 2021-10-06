package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.User;
import java.util.List;

public interface UserService {
    User getUserById(long userId);
    void update(User user);
    List<User> getAll();
}