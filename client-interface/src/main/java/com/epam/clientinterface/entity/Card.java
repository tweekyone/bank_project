package com.epam.clientinterface.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name = "card", schema = "public")
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
    @Enumerated(EnumType.STRING)
    private CardPlan plan;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @OneToOne(mappedBy = "card")
    @JoinColumn(name = "card_id")
    private PinCounter pinCounter;

    public Card(
        @NonNull Account account,
        @NonNull String number,
        @NonNull String pinCode,
        @NonNull CardPlan plan,
        @NonNull LocalDateTime expirationDate,
        PinCounter.Factory pinCounter
    ) {
        this.account = account;
        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.expirationDate = expirationDate;
        this.pinCounter = pinCounter.createFor(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return id.equals(card.id) && number.equals(card.number) && account.equals(card.account) && pinCode.equals(
            card.pinCode) && plan == card.plan && expirationDate.equals(card.expirationDate) && pinCounter.equals(
            card.pinCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, account, pinCode, plan, expirationDate, pinCounter);
    }
}
