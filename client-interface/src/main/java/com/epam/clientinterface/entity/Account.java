package com.epam.clientinterface.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(name = "plan", nullable = false)
    @Enumerated(EnumType.STRING)
    private Plan plan;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public enum Plan {
        TESTPLAN
    }

    public Account(@NonNull User user, @NonNull String number, boolean isDefault, @NonNull Plan plan, double amount) {
        this.user = user;
        this.number = number;
        this.isDefault = isDefault;
        this.plan = plan;
        this.amount = amount;
    }

    public interface Factory {
        Account createFor(User user);
    }

    @AllArgsConstructor
    public static class SimpleFactory implements Factory {
        private final String number;
        private final boolean isDefault;
        private final Plan plan;
        private final double amount;

        public Account createFor(User user) {
            return new Account(user, this.number, this.isDefault, this.plan, this.amount);
        }
    }

    @AllArgsConstructor
    public static class AsFirstFactory implements Factory {
        private final String number;

        public Account createFor(User user) {
            return new Account(user, this.number, true, Plan.TESTPLAN, 0.0);
        }
    }
}
