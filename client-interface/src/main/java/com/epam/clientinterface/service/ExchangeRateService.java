package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.CurrencyNotFoundException;
import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository rateRepository;

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

        for (Currency currency : currencies) {
            if (currency.toString().equalsIgnoreCase(currencyFrom)) {
                curFrom = currency;
            }
            if (currency.toString().equalsIgnoreCase(currencyTo)) {
                curTo = currency;
            }
        }

        Optional<ExchangeRate> oneByCurrencies = rateRepository.findOneByCurrencies(curFrom, curTo);
        if (oneByCurrencies.isEmpty()) {
            throw new CurrencyNotFoundException(currencyFrom, currencyTo);
        }
        double rate = oneByCurrencies.get().getRate();
        Map<String, Double> currenciesRatesMap = new HashMap<>();

        if (rate != 0) {
            currenciesRatesMap.put("rate", rate);
        }
        return currenciesRatesMap;
    }

    private Map<Currency, Double> currencyRatesToMap(List<ExchangeRate> exchangeRates) {
        Map<Currency, Double> currencyMap = new HashMap<>();
        for (ExchangeRate rate : exchangeRates) {
            currencyMap.put(rate.getCurrencyTo(), rate.getRate());
        }
        return currencyMap;
    }
}
