package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository rateRepository;

    public List<ExchangeRate> getRatesForOneCurrency(Currency currency) {
       return rateRepository.getExchangeRatesByCurrencyFrom(currency);
    }

    public Optional<ExchangeRate> getRatesFromToCurrency(Currency currencyFrom, Currency currencyTo) {
        return rateRepository.findOneByCurrencies(currencyFrom, currencyTo);
    }
}
