package com.epam.clientinterface.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne
    private Account account;

    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Column(name = "plan", nullable = false)
    private Plan plan;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    public enum Plan {
        BASE
    }

    public Card(
        @NonNull Account account,
        @NonNull String number,
        @NonNull String pinCode,
        @NonNull Plan plan,
        @NonNull LocalDateTime expirationDate
    ) {
        this.account = account;
        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.expirationDate = expirationDate;
    }
}
