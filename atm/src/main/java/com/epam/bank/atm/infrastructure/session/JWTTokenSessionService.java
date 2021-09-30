package com.epam.bank.atm.infrastructure.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;
import java.util.Calendar;
import java.util.Date;

public class JWTTokenSessionService implements TokenSessionService, TokenService {
    private final JWTTokenPolicy jwtTokenPolicy;
    private final ThreadLocal<SessionBag> sessionBag = ThreadLocal.withInitial(SessionBag::new);
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

        var authDescriptor = this.getAuthDescriptorByToken(token);
        if (authDescriptor == null) {
            return;
        }

        this.sessionBag.get().setToken(token);
        this.sessionBag.get().setAuthDescriptor(authDescriptor);
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
        return this.sessionBag.get().getAuthDescriptor();
    }

    private AuthDescriptor getAuthDescriptorByToken(String token) {
        if (token == null) {
            return null;
        }

        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.decode(token);
        } catch (JWTDecodeException e) {
            return null;
        }

        var subject = decodedJWT.getSubject();

        if (subject == null) {
            return null;
        }

        var cardId = Integer.parseInt(subject);
        var card = this.cardRepository.getById(cardId);

        if (card == null) {
            return null;
        }

        var account = this.accountRepository.getById(card.getAccountId());

        if (account == null) {
            return null;
        }

        var user = this.userRepository.getById(account.getUserId());

        if (user == null) {
            return null;
        }

        return new AuthDescriptor(user, account, card);
    }

    @Override
    public boolean isExpired(String token) {
        if (token == null) {
            throw new RuntimeException("Token is malformed");
        }

        var expiresAt = JWT.decode(token).getExpiresAt();

        if (expiresAt == null) {
            throw new RuntimeException("Token is malformed");
        }

        return expiresAt.before(new Date());
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
