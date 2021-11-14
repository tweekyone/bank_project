package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Short> {

    @Query("select er from ExchangeRate er where er.currencyFrom = ?1 and er.currencyTo = ?2")
    Optional<ExchangeRate> findOneByCurrencies(Currency currencyFrom, Currency currencyTo);

    // @Query("select er.currencyFrom, er.currencyTo, er.rate from ExchangeRate er where er.currencyFrom = ?1")
    // List<ExchangeRate> getExchangeRatesByCurrencyFrom(Currency currencyFrom);

    // @Query(value = "select er.currency_from, er.currency_to, er.rate from exchange_rate er where er.currency_from = ?", nativeQuery = true)
    List<ExchangeRate> getExchangeRatesByCurrencyFrom(Currency currencyFrom);

    ExchangeRate getAllById(short id);
}
