package com.promovac.jolivoyage.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentesParJourDto {
    // Getters et setters
    private LocalDate date;
    private Long nombreVentes;
    private BigDecimal totalVentes;

}
