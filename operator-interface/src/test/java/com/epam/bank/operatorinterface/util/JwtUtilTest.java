package com.epam.bank.operatorinterface.util;

import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.domain.UserDetailsAuthImpl;
import com.epam.bank.operatorinterface.entity.Role;
import com.epam.bank.operatorinterface.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil testingJwtUtil;
    @Mock
    private UserDetailsService mockUserDetailsService;

    private User testUserEntity;
    private String testSecretKey;
    private String testToken;
    private UserDetails testUserDetails;

    @BeforeEach
    public void setUp() {
        testUserEntity = new User(
            1L,
            RandomStringUtils.random(5),
            RandomStringUtils.random(5),
            RandomStringUtils.randomNumeric(9),
            RandomStringUtils.random(6),
            RandomStringUtils.random(5),
            RandomStringUtils.random(5),
            true,
            0,
            Collections.emptyList(),
            Set.of(new Role(1L, "ROLE_ADMIN"))
        );
        testSecretKey = "testSecret";
        testToken = generateToken(new UserDetailsAuthImpl(
            testUserEntity.getPassword(),
            testUserEntity.getEmail(),
            testUserEntity.getRoles(),
            testUserEntity.isEnabled()),
            1000L * 60L * 60L * 24L
        );
        testUserDetails = new UserDetailsAuthImpl(
            testUserEntity.getPassword(),
            testUserEntity.getEmail(),
            testUserEntity.getRoles(),
            testUserEntity.isEnabled()
        );

        Class jwtUtilClass = testingJwtUtil.getClass();
        try {
            Field secretKeyField = jwtUtilClass.getDeclaredField("secretKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(testingJwtUtil, testSecretKey);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void generateTokenShouldReturnNewToken() {
        String result = testingJwtUtil.generateToken(new UserDetailsAuthImpl(
            testUserEntity.getPassword(),
            testUserEntity.getEmail(),
            testUserEntity.getRoles(),
            testUserEntity.isEnabled())
        );

        Assertions.assertThat(testToken).isEqualTo(result);
    }

    @Test
    public void extractUsernameShouldReturnEmail() {
        String result = testingJwtUtil.extractEmail(testToken);

        Assertions.assertThat(testUserEntity.getEmail()).isEqualTo(result);
    }

    @Test
    public void extractExpirationShouldReturnDate() {
        Date result = testingJwtUtil.extractExpiration(testToken);

        Assertions.assertThat(LocalDateTime.now().getDayOfYear() + 1)
            .isEqualTo(result.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfYear());
    }

    @Test
    public void validateTokenShouldReturnTrueIfTokenCorrect() {
        Mockito.when(mockUserDetailsService.loadUserByUsername(Mockito.anyString()))
            .thenReturn(testUserDetails);

        Assertions.assertThat(testingJwtUtil.validateToken(testToken)).isEqualTo(true);
    }

    @Test
    public void validateTokenShouldReturnFalseIfTokenIsExpired() {
        String fakeToken = generateToken(testUserDetails, 0);

        Throwable exception = Assertions.catchThrowable(
            () -> {
                testingJwtUtil.validateToken(fakeToken);
            });

        Assertions.assertThat(exception).isInstanceOf(ExpiredJwtException.class);
    }

    private String generateToken(UserDetails userDetails, long expirationMillis) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), expirationMillis);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
            .signWith(SignatureAlgorithm.HS512, testSecretKey)
            .compact();
    }
}