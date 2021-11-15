package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.CurrencyNotFoundException;
import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository rateRepository;

    public Map<Currency, Double> getRatesForOneCurrency(String currency) {
        List<Currency> currencies = Currency.getCurrencies();

        for (Currency c : currencies) {
            if (c.toString().equalsIgnoreCase(currency)) {
                return currencyRatesToMap(rateRepository.getExchangeRatesByCurrencyFrom(c));
            }
        }
        throw new CurrencyNotFoundException(currency);
    }

    public Map<String, Double> getRatesFromOneToAnotherCurrency(String currencyFrom, String currencyTo) {
        List<Currency> currencies = Currency.getCurrencies();
        Currency curFrom = null;
        Currency curTo = null;

        for (Currency c : currencies) {
            if (c.toString().equalsIgnoreCase(currencyFrom)) {
                curFrom = c;
            }
            if (c.toString().equalsIgnoreCase(currencyTo)) {
                curTo = c;
            }
        }

        Optional<ExchangeRate> oneByCurrencies = rateRepository.findOneByCurrencies(curFrom, curTo);
        if (oneByCurrencies.isEmpty()) {
            throw new CurrencyNotFoundException(currencyFrom, currencyTo);
        }
        double rate = oneByCurrencies.get().getRate();
        Map<String, Double> currenciesRatesMap = new HashMap<>();

        if (rate != 0) {
            currenciesRatesMap.put("exchange rate from "
                    + currencyFrom.toUpperCase() + " to "
                    + currencyTo.toUpperCase(), rate);
        } //else throw new CurrencyNotFoundException();
        return currenciesRatesMap;
    }

    private Map<Currency, Double> currencyRatesToMap(List<ExchangeRate> exchangeRates) {
        Map<Currency, Double> currencyMap = new HashMap<>();
        for (ExchangeRate ex : exchangeRates) {
            currencyMap.put(ex.getCurrencyTo(), ex.getRate());
        }
        return currencyMap;
    }
}
