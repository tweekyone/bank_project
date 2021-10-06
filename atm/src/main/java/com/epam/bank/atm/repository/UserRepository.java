package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.User;
import java.util.List;

public interface UserRepository {
    User getById(long id);
    void save(User user);
    List<User> getAll();
}
