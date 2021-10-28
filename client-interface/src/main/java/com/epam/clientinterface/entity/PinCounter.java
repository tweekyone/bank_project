package com.epam.clientinterface.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PinCounter {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "pinCounter_id_seq", sequenceName = "pinCounter_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pinCounter_id_seq")
    private Long id;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "last_changing_date")
    private LocalDateTime lastChangingDate;

    @Column(name = "change_count")
    private Integer changeCount;
}
