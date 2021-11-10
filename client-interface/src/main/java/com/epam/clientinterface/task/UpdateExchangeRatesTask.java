package com.epam.clientinterface.task;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.money.Monetary;
import javax.money.convert.MonetaryConversions;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateExchangeRatesTask {
    private final ExchangeRateRepository exchangeRateRepository;

    @Scheduled(cron = "${app.task.update_exchange_rates.cron}", zone = "${app.task.update_exchange_rates.zone}")
    public void execute() {
        var exchangeRates = Arrays.stream(Currency.values())
            .map(currency -> MonetaryConversions.getConversion(currency.name()))
            .flatMap(toCurrencyConversion -> Arrays.stream(Currency.values())
                .map(currency -> toCurrencyConversion.getExchangeRate(
                    Monetary.getDefaultAmountFactory().setCurrency(currency.name()).setNumber(1).create()
                ))
            )
            .filter(exchangeRate -> !exchangeRate.getBaseCurrency().getCurrencyCode().equals(
                    exchangeRate.getCurrency().getCurrencyCode()
            ))
            .map(exchangeRate -> {
                var currencyFrom = Currency.valueOf(exchangeRate.getBaseCurrency().getCurrencyCode());
                var currencyTo = Currency.valueOf(exchangeRate.getCurrency().getCurrencyCode());

                var exchangeRateEntity = this.exchangeRateRepository.findOneByCurrencies(
                    currencyFrom,
                    currencyTo
                ).orElseGet(() -> new ExchangeRate(
                    currencyFrom, currencyTo, exchangeRate.getFactor().doubleValue())
                );

                exchangeRateEntity.setRate(exchangeRate.getFactor().doubleValue());

                return exchangeRateEntity;
            })
            .collect(Collectors.toList());

        this.exchangeRateRepository.saveAllAndFlush(exchangeRates);
    }
}
