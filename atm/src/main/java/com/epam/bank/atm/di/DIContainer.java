package com.epam.bank.atm.di;

import com.auth0.jwt.algorithms.Algorithm;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.repository.JDBCTransactionRepository;
import com.epam.bank.atm.infrastructure.session.JWTTokenPolicy;
import com.epam.bank.atm.infrastructure.session.JWTTokenSessionService;
import com.epam.bank.atm.repository.JDBCCardRepository;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.JDBCAccountRepository;
import com.epam.bank.atm.repository.JDBCTUserRepository;
import com.epam.bank.atm.repository.TransactionRepository;
import com.epam.bank.atm.repository.UserRepository;
import com.epam.bank.atm.service.AccountService;
import com.epam.bank.atm.service.AuthService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import com.epam.bank.atm.service.AuthServiceImpl;
import com.epam.bank.atm.service.TransactionService;
import com.epam.bank.atm.service.TransactionalService;
import org.postgresql.ds.PGSimpleDataSource;

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
        this.singletons.putIfAbsent(AccountService.class, this.createAccountService());
        this.singletons.putIfAbsent(TokenSessionService.class, this.createTokenSessionService());
        this.prototypes.putIfAbsent(TokenSessionService.class, this::createTokenSessionService);
        this.singletons.putIfAbsent(UserRepository.class, this.createUserRepository());
        this.prototypes.putIfAbsent(UserRepository.class, this::createUserRepository);
        this.singletons.putIfAbsent(AccountRepository.class, this.createAccountRepository());
        this.prototypes.putIfAbsent(AccountRepository.class, this::createAccountRepository);
        this.singletons.putIfAbsent(CardRepository.class, this.createCardRepository());
        this.prototypes.putIfAbsent(CardRepository.class, this::createCardRepository);
        this.singletons.putIfAbsent(TokenService.class, this.createTokenSessionService());
        this.prototypes.putIfAbsent(TokenService.class, this::createTokenSessionService);
        this.singletons.putIfAbsent(Connection.class, this.createConnection());
        this.prototypes.putIfAbsent(Connection.class, this::createConnection);
        this.singletons.putIfAbsent(TransactionRepository.class, this.createTransactionRepository());
        this.prototypes.putIfAbsent(TransactionRepository.class, this::createTransactionRepository);
        this.singletons.putIfAbsent(CardRepository.class, this.createCardRepository());
        this.prototypes.putIfAbsent(CardRepository.class, this::createCardRepository);
        this.singletons.putIfAbsent(TransactionalService.class, this.createTransactionalService());
        this.prototypes.putIfAbsent(TransactionalService.class, this::createTransactionalService);
        this.singletons.putIfAbsent(AuthService.class, this.createAuthService());
        this.prototypes.putIfAbsent(AuthService.class, this::createAuthService);
    }

    public <U extends T, T> U getSingleton(Class<T> aClass) {
        return (U) this.singletons.computeIfAbsent(aClass, k -> {
            throw new RuntimeException("Service is not configured");
        });
    }

    private <U extends T, T> U getSingleton(Class<T> aClass, Supplier<U> supplier) {
        return (U) this.singletons.computeIfAbsent(aClass, k -> supplier.get());
    }

    public <U extends T, T> U getPrototype(Class<T> aClass) {
        return (U) this.prototypes.computeIfAbsent(aClass, k -> {
            throw new RuntimeException("Service is not configured");
        }).get();
    }

    private <U extends T, T> U getPrototype(Class<T> aClass, Supplier<U> supplier) {
        return (U) this.prototypes.computeIfAbsent(aClass, k -> supplier).get();
    }

    private AuthService createAuthService() {
        return new AuthServiceImpl();
    }

    private UserRepository createUserRepository() {
        return new JDBCTUserRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private AccountRepository createAccountRepository() {
        return new JDBCAccountRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private TokenSessionService createTokenSessionService() {
        return new JWTTokenSessionService(
            new JWTTokenPolicy() {
                @Override
                public int getExpirationPeriod() {
                    return 86400;
                }

                @Override
                public Algorithm getAlgorithm() {
                    return Algorithm.HMAC512("secret");
                }
            },
            this.getSingleton(UserRepository.class, this::createUserRepository),
            this.getSingleton(AccountRepository.class, this::createAccountRepository),
            this.getSingleton(CardRepository.class, this::createCardRepository)
        );
    }

    private Connection createConnection() {
        var dataSource = new PGSimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("123qwe");
        dataSource.setUrl("jdbc:postgresql://postgres:5432/postgres");

        // ToDo: make container for building app in order to work with one url
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            try {
                dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
                return dataSource.getConnection();
            } catch (SQLException exception) {
                throw new RuntimeException(e);
            }
        }
    }

    private TransactionRepository createTransactionRepository() {
        return new JDBCTransactionRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private CardRepository createCardRepository() {
        return new JDBCCardRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private TransactionalService createTransactionalService() {
        return new TransactionService(
            this.getSingleton(TransactionRepository.class, this::createTransactionRepository)
        );
    }

    private AccountService createAccountService() {
        return new AccountService(
            this.getSingleton(TransactionalService.class, this::createTransactionalService),
            this.getSingleton(AccountRepository.class, this::createAccountRepository)
        );
    }
}
