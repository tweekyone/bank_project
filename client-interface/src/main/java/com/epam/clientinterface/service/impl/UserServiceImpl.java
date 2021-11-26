package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.UserDetailAuth;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

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

        // Creating new user with account above
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, firstFactory);

        // saving user and account to database
        repository.save(newUser);

        return newUser;
    }

    //TODO add encoding and setAuthorities into create user
}
