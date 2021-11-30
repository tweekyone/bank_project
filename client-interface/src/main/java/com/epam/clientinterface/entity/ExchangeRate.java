package com.epam.clientinterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "exchange_rate", schema = "public")
public class ExchangeRate {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    private Long id;

    @Column(name = "currency_from", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyFrom;

    @Column(name = "currency_to", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;

    @Setter
    @Column(name = "rate", nullable = false)
    private double rate;

    public ExchangeRate(Currency currencyFrom, Currency currencyTo, double rate) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
    }
}
