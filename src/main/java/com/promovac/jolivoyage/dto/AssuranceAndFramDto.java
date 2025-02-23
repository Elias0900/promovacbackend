package com.promovac.jolivoyage.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssuranceAndFramDto {
    private Double assuranceTotal;
    private Double venteSansAssurance;
    private Double pourcentageAssurance;
    private Double venteFram;
    private Double venteSansFram;
    private Double pourcentageFram;
}
