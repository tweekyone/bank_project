package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User create(String name, String surname, String phoneNumber,
                String username, String email, String rawPassword);
}
