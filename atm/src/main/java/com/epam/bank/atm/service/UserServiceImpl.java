package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.UserRepository;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(long userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("User ID is null!");
        }
        return userRepository.getById(userId);
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null!");
        } else if (user.getId() == 0) {
            throw new IllegalArgumentException("Id is empty!");
        } else if (user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is empty!");
        } else if (user.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Surname is empty!");
        } else if (user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is empty!");
        } else if (user.getPhone_number().isEmpty()) {
            throw new IllegalArgumentException("Phone number is empty!");
        } else if (user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is empty!");
        }

        //TODO
        //userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        //TODO
        //return userRepository.getAll();
        return null;
    }
}
