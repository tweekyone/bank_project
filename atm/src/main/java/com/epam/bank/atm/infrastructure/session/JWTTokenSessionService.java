package com.epam.bank.atm.infrastructure.session;

import com.auth0.jwt.JWT;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;
import java.util.Calendar;

public class JWTTokenSessionService implements TokenSessionService {
    private final JWTTokenPolicy jwtTokenPolicy;
    private final ThreadLocal<SessionBag> sessionBag = new ThreadLocal<>();
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public JWTTokenSessionService(
        JWTTokenPolicy jwtTokenPolicy,
        UserRepository userRepository,
        AccountRepository accountRepository,
        CardRepository cardRepository
    ) {
        this.jwtTokenPolicy = jwtTokenPolicy;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void initFromToken(String token) {
        if (token == null) {
            return;
        }

        this.sessionBag.get().setToken(token);
        this.sessionBag.get().setAuthDescriptor(this.getAuthDescriptorByToken(token));
    }

    @Override
    public String start(AuthDescriptor authDescriptor) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.jwtTokenPolicy.getExpirationPeriod());

        return JWT
            .create()
            .withExpiresAt(calendar.getTime())
            .withSubject(String.valueOf(authDescriptor.getCard().getId()))
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

    private AuthDescriptor getAuthDescriptorByToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is malformed");
        }

        var subject = JWT.decode(token).getSubject();

        if (subject == null) {
            throw new RuntimeException("Token is malformed");
        }

        var cardId = Integer.parseInt(subject);
        var card = this.cardRepository.getById(cardId);

        if (card == null) {
            throw new RuntimeException("Token is malformed");
        }

        var account = this.accountRepository.getById(card.getAccountId());

        if (account == null) {
            throw new RuntimeException("Token is malformed");
        }

        var user = this.userRepository.getById(account.getUserId());

        if (user == null) {
            throw new RuntimeException("Token is malformed");
        }

        return new AuthDescriptor(user, account, card);
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
