package com.epam.clientinterface.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
@Table(name = "card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardEntity {

    @Id
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "pin_code")
    private String pin;

    @Column(name = "plan")
    @Enumerated(EnumType.STRING)
    private CardPlans plan;

    @Column(name = "explication_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime explicationDate;

    @Column(name = "account_id")
    private Integer accountId;
}
