package com.epam.bank.atm.infrastructure.session;

import com.auth0.jwt.JWT;
import com.epam.bank.atm.controller.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import java.util.Calendar;

public class JWTTokenSessionService implements TokenSessionService {
    private final JWTTokenPolicy jwtTokenPolicy;
    private ThreadLocal<SessionBag> sessionBag = new ThreadLocal<>();

    public JWTTokenSessionService(JWTTokenPolicy jwtTokenPolicy) {
        this.jwtTokenPolicy = jwtTokenPolicy;
    }

    @Override
    public void initFromToken(String token) {
        if (token == null) {
            return;
        }

        this.sessionBag.get().setToken(token);
        this.sessionBag.get().setAuthDescriptor(this.getAuthDescriptorByToken(token));
    }

    // ToDo: provide account id after account has been developed
    @Override
    public String start(AuthDescriptor authDescriptor) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.jwtTokenPolicy.getExpirationPeriod());

        return JWT
            .create()
            .withExpiresAt(calendar.getTime())
            .withSubject("accountId")
            .sign(this.jwtTokenPolicy.getAlgorithm());
    }

    @Override
    public AuthDescriptor curSession() {
        var authDescriptor = this.sessionBag.get().getAuthDescriptor();
        if (authDescriptor == null) {
            throw new RuntimeException("Session is empty");
        }

        return authDescriptor;
    }

    // ToDo: implement this method after account and user have been developed
    private AuthDescriptor getAuthDescriptorByToken(String token) {
        return new AuthDescriptor();
    }

    class SessionBag {
        private AuthDescriptor authDescriptor;
        private String token;

        public AuthDescriptor getAuthDescriptor() {
            return this.authDescriptor;
        }

        public String getToken() {
            return this.token;
        }

        public void setAuthDescriptor(AuthDescriptor authDescriptor) {
            this.authDescriptor = authDescriptor;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
