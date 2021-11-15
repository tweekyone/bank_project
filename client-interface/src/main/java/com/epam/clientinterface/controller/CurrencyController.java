package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.service.ExchangeRateService;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange-rates")
public class CurrencyController {

    private final ExchangeRateService rateService;

    @Autowired
    public CurrencyController(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/get")
    public String testCurrencyRates() {
        return "currency";
    }

    @GetMapping("/{from}")
    public ResponseEntity<Map<Currency, Double>> getCurrencyRates(@Valid @PathVariable("from") String currency) {

        Map<Currency, Double> exchangeRates = rateService.getRatesForOneCurrency(currency);

        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    @GetMapping("{from}/{to}")
    public ResponseEntity<Map<String, Double>> getExchangeRates(
        @Valid @PathVariable("from") String currencyFrom,
        @Valid @PathVariable("to") String currencyTo
    ) {
        Map<String, Double> rateFromOneToAnotherCurrency
            = rateService.getRatesFromOneToAnotherCurrency(currencyFrom, currencyTo);

        return new ResponseEntity<>(rateFromOneToAnotherCurrency, HttpStatus.OK);
    }
}
