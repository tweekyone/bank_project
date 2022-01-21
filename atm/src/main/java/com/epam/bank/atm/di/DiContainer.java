package com.epam.bank.atm.di;

import com.auth0.jwt.algorithms.Algorithm;
import com.epam.bank.atm.controller.session.TokenService;
import com.epam.bank.atm.controller.session.TokenSessionService;
import com.epam.bank.atm.infrastructure.session.JwtTokenPolicy;
import com.epam.bank.atm.infrastructure.session.JwtTokenSessionService;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.JdbcAccountRepository;
import com.epam.bank.atm.repository.JdbcCardRepository;
import com.epam.bank.atm.repository.JdbcTransactionRepository;
import com.epam.bank.atm.repository.JdbcUserRepository;
import com.epam.bank.atm.repository.TransactionRepository;
import com.epam.bank.atm.repository.UserRepository;
import com.epam.bank.atm.service.AccountService;
import com.epam.bank.atm.service.AuthService;
import com.epam.bank.atm.service.AuthServiceImpl;
import com.epam.bank.atm.service.TransactionService;
import com.epam.bank.atm.service.TransactionalService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.postgresql.ds.PGSimpleDataSource;

public class DiContainer {
    private static volatile DiContainer instance = instance();
    private final ConcurrentHashMap<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, Supplier<?>> prototypes = new ConcurrentHashMap<>();
    private final Properties properties = new Properties();

    public static DiContainer instance() {
        if (instance == null) {
            synchronized (DiContainer.class) {
                if (instance == null) {
                    instance = new DiContainer();
                    instance.init();
                }
            }
        }

        return instance;
    }

    private void init() {
        this.initProperties();

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
        this.singletons.putIfAbsent(Liquibase.class, this.createLiquibase());
        this.prototypes.putIfAbsent(Liquibase.class, this::createLiquibase);
    }

    private void initProperties() {
        try {
            this.properties.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")
            );

            for (var entry: this.properties.entrySet()) {
                if (System.getProperties().containsKey(entry.getKey())) {
                    this.properties.setProperty((String) entry.getKey(), System.getProperty((String) entry.getKey()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <U extends T, T> U getSingleton(Class<T> classType) {
        return (U) this.singletons.computeIfAbsent(classType, k -> {
            throw new RuntimeException("Service is not configured");
        });
    }

    private <U extends T, T> U getSingleton(Class<T> classType, Supplier<U> supplier) {
        return (U) this.singletons.computeIfAbsent(classType, k -> supplier.get());
    }

    public <U extends T, T> U getPrototype(Class<T> classType) {
        return (U) this.prototypes.computeIfAbsent(classType, k -> {
            throw new RuntimeException("Service is not configured");
        }).get();
    }

    private <U extends T, T> U getPrototype(Class<T> classType, Supplier<U> supplier) {
        return (U) this.prototypes.computeIfAbsent(classType, k -> supplier).get();
    }

    private AuthService createAuthService() {
        return new AuthServiceImpl(
            this.getSingleton(UserRepository.class, this::createUserRepository),
            this.getSingleton(AccountRepository.class, this::createAccountRepository),
            this.getSingleton(CardRepository.class, this::createCardRepository)
        );
    }

    private UserRepository createUserRepository() {
        return new JdbcUserRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private AccountRepository createAccountRepository() {
        return new JdbcAccountRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private TokenSessionService createTokenSessionService() {
        return new JwtTokenSessionService(
            new JwtTokenPolicy() {
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
        try {
            var dataSource = new PGSimpleDataSource();
            dataSource.setUser(this.properties.getProperty("database.username"));
            dataSource.setPassword(this.properties.getProperty("database.password"));
            dataSource.setUrl(this.properties.getProperty("database.url"));
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Liquibase createLiquibase() {
        var liquibaseConnection = new JdbcConnection(this.getSingleton(Connection.class, this::createConnection));
        try {
            return new Liquibase(
                "liquibase/changelog-master.xml", new ClassLoaderResourceAccessor(), liquibaseConnection);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionRepository createTransactionRepository() {
        return new JdbcTransactionRepository(this.getSingleton(Connection.class, this::createConnection));
    }

    private CardRepository createCardRepository() {
        return new JdbcCardRepository(this.getSingleton(Connection.class, this::createConnection));
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
