package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.domain.UserDetailAuth;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Role;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.UserService;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserDetailAuth userDetailAuth = findByEmail(userEmail)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with userEmail - %s not found", userEmail)));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(userEmail)
            .password(userDetailAuth.getPassword())
            .disabled(!userDetailAuth.isEnabled())
            .authorities(userDetailAuth.getAuthorities())
            .build();
        return userDetails;
    }

    @Transactional
    public Optional<UserDetailAuth> findByEmail(String email) {
        return repository.findByEmailWithRoles(email).map(UserDetailAuth::new);
    }

    @Override
    public User create(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) {

        // Creating bank account for new user with default values and number consists of 20 random integers
        Account.AsFirstFactory firstFactory = new Account.AsFirstFactory(RandomStringUtils.randomNumeric(20));

        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, firstFactory);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRoles(Set.of(new Role(1L, "USER")));

        // saving user and account to database
        repository.save(newUser);

        return newUser;
    }
}
