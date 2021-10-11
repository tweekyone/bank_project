package com.epam.bank.atm.service;

import java.math.BigInteger;

public interface TransactionalService {
    boolean createTransaction(BigInteger source_number, BigInteger destination_number, double amount,
                              String operation_type);
}
