package com.epam.bank.atm.infrastructure.session;

import com.auth0.jwt.algorithms.Algorithm;

public interface JwtTokenPolicy {
    int getExpirationPeriod();

    Algorithm getAlgorithm();
}
