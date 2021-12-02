package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.CurrencyNotFoundException;
import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    private final ExchangeRate exchangeRate = new ExchangeRate(
        (short) RandomUtils.nextInt(), getRandomCurrency(),
        getRandomCurrency(), RandomUtils.nextDouble(0, 999));

    private final List<ExchangeRate> rateList = new ArrayList<>();
    private final Map<String, Double> currencyMap = new HashMap<>();

    @Mock
    private ExchangeRateRepository rateRepository;

    @InjectMocks
    ExchangeRateService exchangeRateService;


    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldReturnRatesForOneCurrency(Currency currency) {
        rateList.add(exchangeRate);

        when(rateRepository.getExchangeRatesByCurrencyFrom(currency))
            .thenReturn(rateList);

        Assertions.assertEquals(
            currencyRatesToMap(rateList),
            exchangeRateService.getRatesForOneCurrency(currency.name())
        );
        verify(rateRepository).getExchangeRatesByCurrencyFrom(any());
    }

    @Test
    void shouldReturnRatesFromOneToAnotherCurrency() {
        Optional<ExchangeRate> oneByCurrencies = Optional.of(exchangeRate);
        Currency currencyFrom = getRandomCurrency();
        Currency currencyTo = getRandomCurrency();
        currencyMap.put("rate", oneByCurrencies.get().getRate());

        when(rateRepository.findOneByCurrencies(currencyFrom, currencyTo))
            .thenReturn(oneByCurrencies);

        Map<String, Double> ratesFromOneToAnotherCurrency =
            exchangeRateService.getRatesFromOneToAnotherCurrency(currencyFrom.name(), currencyTo.name());

        Assertions.assertEquals(currencyMap, ratesFromOneToAnotherCurrency);
        verify(rateRepository).findOneByCurrencies(any(), any());
    }

    @Test
    void shouldThrowCurrencyNotFoundIfOneOfExchangeCurrenciesNotFound() {
        Optional<ExchangeRate> emptyCurrency = Optional.empty();
        String randomInput1 = RandomStringUtils.randomPrint(2, 5);
        String randomInput2 = RandomStringUtils.randomPrint(2, 5);

        when(rateRepository.findOneByCurrencies(any(), any()))
            .thenReturn(emptyCurrency);

        Assertions.assertThrows(CurrencyNotFoundException.class,
            () -> exchangeRateService.getRatesFromOneToAnotherCurrency(randomInput1, randomInput2)
        );
        verify(rateRepository).findOneByCurrencies(any(), any());
    }

    private static Currency getRandomCurrency() {
        List<Currency> currencies = Currency.getCurrencies();
        return currencies.get(new Random().nextInt(currencies.size()));
    }

    private Map<Currency, Double> currencyRatesToMap(List<ExchangeRate> exchangeRates) {
        Map<Currency, Double> currencyMap = new HashMap<>();
        for (ExchangeRate rate : exchangeRates) {
            currencyMap.put(rate.getCurrencyTo(), rate.getRate());
        }
        return currencyMap;
    }
}