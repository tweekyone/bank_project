package com.epam.bank.operatorinterface.config;

import com.epam.bank.operatorinterface.configuration.security.util.JwtAuthenticationToken;
import com.epam.bank.operatorinterface.domain.UserDetailsAuthImpl;
import com.epam.bank.operatorinterface.entity.Role;
import java.time.Instant;
import java.util.Set;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class AdminMockSecurityContextFactory implements WithSecurityContextFactory<WithMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(WithMockAdmin mockAdmin) {
        Jwt jwt = Jwt.withTokenValue("token")
            .headers(h -> h.put("k", "v"))
            .claims(c -> c.put("k", "v"))
            .issuedAt(Instant.now())
            .build();

        UserDetailsAuthImpl userDetailsAuth = new UserDetailsAuthImpl(
            mockAdmin.password(),
            mockAdmin.email(),
            Set.of(new Role(1L, "ROLE_" + mockAdmin.role())),
            true
        );

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
            userDetailsAuth,
            jwt.getTokenValue(),
            true
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
