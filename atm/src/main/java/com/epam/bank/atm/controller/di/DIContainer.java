package com.epam.bank.atm.controller.di;

import com.auth0.jwt.algorithms.Algorithm;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.infrastructure.session.JWTTokenPolicy;
import com.epam.bank.atm.infrastructure.session.JWTTokenSessionService;
import com.epam.bank.atm.service.AuthService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DIContainer {
    private static volatile DIContainer instance = instance();
    private final ConcurrentHashMap<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, Supplier<?>> prototypes = new ConcurrentHashMap<>();

    public static DIContainer instance() {
        if (instance == null) {
            synchronized (DIContainer.class) {
                if (instance == null) {
                    instance = new DIContainer();
                    instance.init();
                }
            }
        }

        return instance;
    }

    private void init() {
        this.singletons.putIfAbsent(AuthService.class, this.createAuthService());
        this.prototypes.putIfAbsent(AuthService.class, this::createAuthService);
        this.singletons.putIfAbsent(TokenSessionService.class, this.createTokenSessionService());
        this.prototypes.putIfAbsent(TokenSessionService.class, this::createTokenSessionService);
    }

    public <U extends T, T> U getSingleton(Class<T> aClass) {
        return (U) singletons.computeIfAbsent(aClass, k -> {
            throw new RuntimeException("Service is not configured");
        });
    }

    public <U extends T, T> U getPrototype(Class<T> aClass) {
        return (U) prototypes.computeIfAbsent(aClass, k -> {
            throw new RuntimeException("Service is not configured");
        }).get();
    }

    // ToDo: implement service interface after repository and entities have created
    private AuthService createAuthService() {
        return new AuthService() {
            @Override
            public AuthDescriptor login(String cardNumber, String pin) {
                return new AuthDescriptor();
            }
        };
    }

    private TokenSessionService createTokenSessionService() {
        return new JWTTokenSessionService(new JWTTokenPolicy() {
            @Override
            public int getExpirationPeriod() {
                return 86400;
            }

            @Override
            public Algorithm getAlgorithm() {
                return Algorithm.HMAC512("secret");
            }
        });
    }
}
