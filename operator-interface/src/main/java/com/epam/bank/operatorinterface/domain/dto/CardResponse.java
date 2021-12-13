package com.epam.bank.operatorinterface.domain.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardResponse {
    @NotNull
    @Positive
    Long id;
    @NotBlank
    @Size(min = 16, max = 16)
    String cardNumber;
    @NotNull
    LocalDate expirationDate;
}
