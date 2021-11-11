package com.epam.clientinterface.controller;

import com.epam.clientinterface.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/exchange-rates")
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencyRepository;

    private Map<String, Double> currencyRates = new HashMap<>(30);

    @GetMapping("/get")
    public String testCurrencyRates() {
        return "currency";
    }

    @GetMapping("/{from}")
    public ResponseEntity<Map<String, Double>> getCurrencyRates(@PathVariable("from") String currency) {

        System.out.println(currencyRates);
        double currencyRate = currencyRepository.getCurrencyRate((short) 1);
        currencyRates.put(currency, currencyRate);
        return new ResponseEntity<>(currencyRates, HttpStatus.OK);
    }

    @GetMapping("{from}/{to}")
    public ResponseEntity<Map<String, Double>> getExchangeRates(
        @PathVariable("from") String currencyFrom,
        @PathVariable("to") String currencyTo
    ) {
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put(currencyTo, currencyRepository.getCurrencyRateByName(currencyTo));
        System.out.println(exchangeRates);
        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }
}
