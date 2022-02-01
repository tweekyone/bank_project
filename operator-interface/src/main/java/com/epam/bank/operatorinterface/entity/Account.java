package com.epam.bank.operatorinterface.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account", schema = "public")
public class Account {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private AccountPlan plan;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinTable(name = "card", joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> cards = new ArrayList<>();

    @Column(name = "closed_at", nullable = false)
    private LocalDateTime closedAt;

    public Account(User user, String number, boolean isDefault, AccountPlan plan) {
        this.user = user;
        this.number = number;
        this.isDefault = isDefault;
        this.plan = plan;
        this.amount = 0D;
    }

    public void close() {
        closedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return closedAt == null;
    }

    public boolean isNotDefault() {
        return !isDefault();
    }

    public void makeDefault() {
        isDefault = true;
    }

    public void makeNotDefault() {
        isDefault = false;
    }

    public boolean isClosed() {
        return closedAt.toLocalDate().isBefore(LocalDate.now());
    }
}
