package com.epam.bank.atm.infrastructure.session;

import com.auth0.jwt.algorithms.Algorithm;

public interface JWTTokenPolicy {
    int getExpirationPeriod();

    Algorithm getAlgorithm();
}
