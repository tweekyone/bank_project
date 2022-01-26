package com.epam.bank.operatorinterface.entity;

import com.epam.bank.operatorinterface.enumerated.CardPlan;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private CardPlan plan;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;

    @Column(name = "pin_counter", nullable = false)
    private Integer pinCounter;

    @Transient
    private boolean isPinChanged = false;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Card(String number, String pinCode, CardPlan plan,
                boolean isBlocked, ZonedDateTime expirationDate, Account account) {
        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.isBlocked = isBlocked;
        this.expirationDate = expirationDate;
        this.pinCounter = 0;
        this.account = account;
    }

    public boolean isClosed() {
        return expirationDate.toLocalDate().isBefore(LocalDate.now());
    }

    @Transient
    public void changePinCode(String pinCode) {
        this.pinCode = pinCode;
        this.isPinChanged = true;
    }

    @PreUpdate
    @PrePersist
    public void onSave() {
        if (this.isPinChanged) {
            this.pinCounter++;
            this.isPinChanged = false;
        }
    }
}
