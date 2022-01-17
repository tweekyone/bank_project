package com.epam.bank.operatorinterface.entity;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne
    private Account account;

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

    public Card(
        @NonNull Account account,
        @NonNull String number,
        @NonNull String pinCode,
        @NonNull CardPlan plan
    ) {
        this.account = account;
        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.expirationDate = ZonedDateTime.now().plusYears(3);
        this.isBlocked = false;
        this.pinCounter = 0;
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
