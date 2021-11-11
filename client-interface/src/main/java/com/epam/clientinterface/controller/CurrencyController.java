package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
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

    // @Autowired
    // private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository rateRepository;

    // private Map<String, Double> currencyRates = new HashMap<>();

    @GetMapping("/get")
    public String testCurrencyRates() {
        return "currency";
    }

    @GetMapping("/{from}")
    public ResponseEntity<ExchangeRate> getCurrencyRates(@PathVariable("from") String currency) {

        ExchangeRate exchangeRate1 = rateRepository.getAllById((short) 1);

        return new ResponseEntity<>(exchangeRate1, HttpStatus.OK);
    }

    @GetMapping("{from}/{to}")
    public ResponseEntity<ExchangeRate> getExchangeRates(
        @PathVariable("from") String currencyFrom,
        @PathVariable("to") String currencyTo
    ) {


        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
