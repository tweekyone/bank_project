package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.User;

public interface UserService {
    User create(String name, String surname, String phoneNumber,
                String username, String email, String rawPassword);
}
