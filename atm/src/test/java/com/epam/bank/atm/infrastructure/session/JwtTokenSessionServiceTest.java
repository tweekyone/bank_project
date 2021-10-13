package com.epam.bank.atm.infrastructure.session;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.bank.atm.BaseTest;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtTokenSessionServiceTest extends BaseTest {
    @Test
    void shouldGenerateValidToken() {
        var sessionService = this.initSessionService(null, null, null, null);
        var token = sessionService.start(this.getTestAuthDescriptor());

        Assertions.assertNotNull(token);
        Assertions.assertDoesNotThrow(() -> JWT.decode(token));

        var decodedToken = JWT.decode(token);

        Assertions.assertNotNull(decodedToken.getSubject());
        Assertions.assertNotNull(decodedToken.getExpiresAt());
        Assertions.assertTrue(decodedToken.getExpiresAt().after(new Date()));
    }

    @Test
    void shouldInitSessionFromTokenIfTokenIsCorrect() {
        var sessionService = this.initSessionService(null, null, null, null);
        var token = sessionService.start(this.getTestAuthDescriptor());

        sessionService.initFromToken(token);

        Assertions.assertNotNull(sessionService.curSession());
    }

    @Test
    void shouldNotInitSessionFromTokenIfTokenIsInvalid() {
        var sessionService = this.initSessionService(null, null, null, null);

        sessionService.initFromToken("asf");

        Assertions.assertNull(sessionService.curSession());
    }

    @Test
    void shouldReturnFalseIfTokenIsNotExpired() {
        var sessionService = this.initSessionService(null, null, null, null);
        var token = sessionService.start(this.getTestAuthDescriptor());

        Assertions.assertFalse(((TokenService) sessionService).isExpired(token));
    }

    @Test
    void shouldReturnTrueIfTokenIsExpired() throws InterruptedException {
        var jwtTokenPolicy = this.getCommonMockForJwtTokenPolicy();
        when(jwtTokenPolicy.getExpirationPeriod()).thenReturn(1);

        var sessionService = this.initSessionService(jwtTokenPolicy, null, null, null);
        var token = sessionService.start(this.getTestAuthDescriptor());

        Thread.sleep(2000);

        Assertions.assertTrue(((TokenService) sessionService).isExpired(token));
    }

    private TokenSessionService initSessionService(
        JwtTokenPolicy jwtTokenPolicy,
        UserRepository userRepository,
        AccountRepository accountRepository,
        CardRepository cardRepository
    ) {
        return new JwtTokenSessionService(
            jwtTokenPolicy != null ? jwtTokenPolicy : this.getCommonMockForJwtTokenPolicy(),
            userRepository != null ? userRepository : this.getCommonMockForUserRepository(),
            accountRepository != null ? accountRepository : this.getCommonMockForAccountRepository(),
            cardRepository != null ? cardRepository : this.getCommonMockForCardRepository()
        );
    }

    private JwtTokenPolicy getCommonMockForJwtTokenPolicy() {
        var jwtTokenPolicy = mock(JwtTokenPolicy.class);
        when(jwtTokenPolicy.getExpirationPeriod()).thenReturn(3600);
        when(jwtTokenPolicy.getAlgorithm()).thenReturn(Algorithm.HMAC512("secret"));

        return jwtTokenPolicy;
    }

    private UserRepository getCommonMockForUserRepository() {
        var userRepository = mock(UserRepository.class);
        when(userRepository.getById(anyLong())).thenReturn(this.getTestUser());

        return userRepository;
    }

    private AccountRepository getCommonMockForAccountRepository() {
        var accountRepository = mock(AccountRepository.class);
        when(accountRepository.getById(anyLong())).thenReturn(this.getTestAccount());

        return accountRepository;
    }

    private CardRepository getCommonMockForCardRepository() {
        var cardRepository = mock(CardRepository.class);
        when(cardRepository.getById(anyLong())).thenReturn(Optional.of(this.getTestCard()));

        return cardRepository;
    }

    private AuthDescriptor getTestAuthDescriptor() {
        return new AuthDescriptor(this.getTestUser(), this.getTestAccount(), this.getTestCard());
    }

    private User getTestUser() {
        return new User(1L, "name", "surname", "email@mail.com", "username", "phone number");
    }

    private Account getTestAccount() {
        return new Account(1L, 1L, true, "plan", 10000, 1L);
    }

    private Card getTestCard() {
        return new Card(1L, "123456", 1L, "1234", Card.Plan.TESTPLAN, LocalDateTime.now());
    }
}
