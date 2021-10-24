package com.epam.clientinterface.entity;

import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
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
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "transaction", schema = "public")
public class Transaction {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    private Long id;

    @Nullable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "accountNumber", column = @Column(name = "source_account_number")),
        @AttributeOverride(name = "isExternal", column = @Column(name = "source_is_external"))
    })
    private AccountData sourceAccount;

    @Nullable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "accountNumber", column = @Column(name = "destination_account_number")),
        @AttributeOverride(name = "isExternal", column = @Column(name = "destination_is_external"))
    })
    private AccountData destinationAccount;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "operation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Getter
    @NoArgsConstructor
    @Embeddable
    public static class AccountData {
        private String accountNumber;
        private boolean isExternal;

        public AccountData(@NonNull String accountNumber, boolean isExternal) {
            this.accountNumber = accountNumber;
            this.isExternal = isExternal;
        }
    }

    public enum OperationType {
        INNER_TRANSFER, EXTERNAL_TRANSFER
    }

    public enum State {
        SUCCESS, DECLINE
    }

    public Transaction(
        @NonNull AccountData sourceAccount,
        @NonNull AccountData destinationAccount,
        double amount,
        @NonNull OperationType operationType,
        @NonNull State state
    ) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.operationType = operationType;
        this.state = state;
    }
}
