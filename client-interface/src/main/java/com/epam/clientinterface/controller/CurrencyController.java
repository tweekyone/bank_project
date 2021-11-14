package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exchange-rates")
public class CurrencyController {

    private final ExchangeRateService rateService;

    @Autowired
    public CurrencyController(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    // @Autowired
    // private ExchangeRateRepository rateRepository;

    @GetMapping("/get")
    public String testCurrencyRates() {
        return "currency";
    }

    @GetMapping("/{from}")
    public ResponseEntity<List<ExchangeRate>> getCurrencyRates(@PathVariable("from") Currency currency) {

        List<ExchangeRate> exchangeRates = rateService.getRatesForOneCurrency(currency);

        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    @GetMapping("{from}/{to}")
    public ResponseEntity<ExchangeRate> getExchangeRates(
         @PathVariable("from") Currency currencyFrom,
         @NotNull @PathVariable("to") Currency currencyTo
    ) {
        Optional<ExchangeRate> oneByCurrencies = rateService.getRatesFromToCurrency(currencyFrom, currencyTo);

        return new ResponseEntity<>(oneByCurrencies.get(), HttpStatus.OK);
    }
}
