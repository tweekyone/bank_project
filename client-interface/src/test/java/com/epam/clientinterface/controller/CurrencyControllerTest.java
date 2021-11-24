package com.epam.clientinterface.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.clientinterface.controller.advice.ErrorHandlingAdvice;
import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    private final String url = "/exchange-rates";
    private final Map<String, Double> currencyStringMap = new HashMap<>();
    private final Map<Currency, Double> currencyMap = new HashMap<>();

    private MockMvc mockMvc;

    @Mock
    ExchangeRateService rateService;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(new CurrencyController(rateService))
            .setControllerAdvice(ErrorHandlingAdvice.class)
            .build();
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldReturnCurrencyRates(Currency currency) throws Exception {
        when(rateService.getRatesForOneCurrency(currency.name()))
            .thenReturn(currencyMap);

        currencyMap.put(currency, RandomUtils.nextDouble(0, 999));
        String mapToJson = new ObjectMapper().writeValueAsString(currencyMap);

        mockMvc.perform(get(url + "/{currency}", currency))
            .andExpect(status().isOk())
            .andExpect(content().string(mapToJson));

        verify(rateService).getRatesForOneCurrency(currency.name());
    }

    @Test
    void shouldReturnExchangeRatesBetweenCurrencies() throws Exception {
        String currencyFrom = getRandomCurrency().name();
        String currencyTo = getRandomCurrency().name();

        when(rateService
            .getRatesFromOneToAnotherCurrency(currencyFrom, currencyTo))
            .thenReturn(currencyStringMap);

        System.out.println(currencyFrom);
        System.out.println(currencyTo);

        currencyStringMap.put(currencyFrom, RandomUtils.nextDouble(0, 999));
        currencyStringMap.put(currencyTo, RandomUtils.nextDouble(0, 999));
        String mapToJson = new ObjectMapper().writeValueAsString(currencyStringMap);

        mockMvc.perform(get(url + "/{from}/{to}", currencyFrom, currencyTo))
            .andExpect(status().isOk())
            .andExpect(content().string(mapToJson));

        verify(rateService).getRatesFromOneToAnotherCurrency(any(), any());
    }
    //TODO 2 tests with exception curr not found

    private static Currency getRandomCurrency() {
        List<Currency> currencies = Currency.getCurrencies();
        return currencies.get(new Random().nextInt(currencies.size()));
    }
}