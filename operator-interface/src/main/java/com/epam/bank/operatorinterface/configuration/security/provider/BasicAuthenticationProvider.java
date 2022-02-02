package com.epam.bank.operatorinterface.configuration.security.provider;

import com.epam.bank.operatorinterface.configuration.security.util.JwtAuthenticationToken;
import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String providedUsername = authentication.getPrincipal().toString();
        UserDetails user = userService.loadUserByUsername(providedUsername);

        String providedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(providedPassword, user.getPassword())) {
            throw new BadCredentialsException("Incorrect Credentials");
        }

        return new JwtAuthenticationToken(user, jwtUtil.generateToken(user), true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
