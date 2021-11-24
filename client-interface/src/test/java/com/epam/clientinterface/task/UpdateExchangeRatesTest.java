package com.epam.clientinterface.task;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.entity.Currency;
import com.epam.clientinterface.entity.ExchangeRate;
import com.epam.clientinterface.repository.ExchangeRateRepository;
import com.epam.clientinterface.task.UpdateExchangeRatesTask;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateExchangeRatesTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepositoryMock;

    @InjectMocks
    private UpdateExchangeRatesTask task;

    @Captor
    private ArgumentCaptor<List<ExchangeRate>> exchangeRates;

    @BeforeAll
    public static void beforeAll() {
        var monetaryConversionsMock = Mockito.mockStatic(MonetaryConversions.class);

        Arrays.stream(Currency.values()).forEach(currency -> {
            var currencyConversionMock = Mockito.mock(CurrencyConversion.class);
            var currencyUnitMock = Mockito.mock(CurrencyUnit.class);
            when(currencyUnitMock.getCurrencyCode()).thenReturn(currency.name());
            when(currencyConversionMock.getExchangeRate(Mockito.any(MonetaryAmount.class))).then(invocation -> {
                var monetary = (MonetaryAmount) invocation.getArgument(0);
                var exchangeRateMock = Mockito.mock(javax.money.convert.ExchangeRate.class);
                var numberValueMock = Mockito.mock(NumberValue.class);
                lenient().when(numberValueMock.doubleValue()).thenReturn(RandomUtils.nextDouble());
                when(exchangeRateMock.getBaseCurrency()).thenReturn(currencyUnitMock);
                when(exchangeRateMock.getCurrency()).thenReturn(monetary.getCurrency());
                lenient().when(exchangeRateMock.getFactor()).thenReturn(numberValueMock);

                return exchangeRateMock;
            });

            monetaryConversionsMock
                .when(() -> MonetaryConversions.getConversion(currency.name()))
                .thenReturn(currencyConversionMock);
        });
    }

    @Test
    public void shouldCollectAllCombinationsOfCurrenciesAndSaveThemWithEmptyRepository() {
        this.task.execute();

        verify(this.exchangeRateRepositoryMock).saveAllAndFlush(this.exchangeRates.capture());

        Assertions.assertEquals(
            Currency.values().length * (Currency.values().length - 1),
            this.exchangeRates.getValue().size()
        );
    }

    @Test
    public void shouldCollectAllCombinationsOfCurrenciesAndSaveThemWithFullRepository() {
        for (var currencyA : Currency.values()) {
            for (var currencyB : Currency.values()) {
                if (currencyA.equals(currencyB)) {
                    continue;
                }

                when(this.exchangeRateRepositoryMock.findExchangeRateByCurrencyFromAndCurrencyTo(currencyA, currencyB))
                    .thenReturn(Optional.of(new ExchangeRate(1L, currencyA, currencyB, RandomUtils.nextDouble())));
            }
        }

        this.task.execute();

        verify(this.exchangeRateRepositoryMock).saveAllAndFlush(this.exchangeRates.capture());

        Assertions.assertEquals(
            Currency.values().length * (Currency.values().length - 1),
            this.exchangeRates.getValue().size()
        );
    }
}
