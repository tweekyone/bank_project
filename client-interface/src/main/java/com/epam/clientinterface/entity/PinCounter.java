package com.epam.clientinterface.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pin_counter", schema = "public")
public class PinCounter {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "pinCounter_id_seq", sequenceName = "pinCounter_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pinCounter_id_seq")
    private Long id;

    @OneToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Column(name = "last_changing_date")
    private LocalDateTime lastChangingDate;

    @Column(name = "change_count")
    private Integer changeCount;

    public interface Factory {
        PinCounter createFor(Card card);
    }

    public PinCounter(@NonNull Card card, @NonNull LocalDateTime lastChangingDate, Integer changeCount) {
        this.card = card;
        this.lastChangingDate = lastChangingDate;
        this.changeCount = changeCount;
    }

    @AllArgsConstructor
    public static class SimpleFactory implements Factory {
        private LocalDateTime lastChangingDate;
        private Integer changeCount;

        @Override
        public PinCounter createFor(Card card) {
            return new PinCounter(card, this.lastChangingDate, this.changeCount);
        }
    }

    @AllArgsConstructor
    public static class AsFirstFactory implements Factory {
        private LocalDateTime lastChangingDate;

        @Override
        public PinCounter createFor(Card card) {
            return new PinCounter(card, this.lastChangingDate, 0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PinCounter that = (PinCounter) o;
        return id.equals(that.id) && card.equals(that.card) && lastChangingDate.equals(that.lastChangingDate)
            && changeCount.equals(that.changeCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, card, lastChangingDate, changeCount);
    }
}
