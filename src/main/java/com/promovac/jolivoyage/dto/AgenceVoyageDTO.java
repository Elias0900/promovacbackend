package com.promovac.jolivoyage.dto;

import com.promovac.jolivoyage.entity.AgenceVoyage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgenceVoyageDTO {
    private Long id;
    private String nom;
    private double objectif;

    public static AgenceVoyageDTO mapToDTO(AgenceVoyage agenceVoyage) {
        if (agenceVoyage == null) {
            return null;
        }

        return AgenceVoyageDTO.builder()
                .id(agenceVoyage.getId())
                .nom(agenceVoyage.getNom())
                .objectif(agenceVoyage.getObjectif())
                .build();
    }

    public AgenceVoyage mapToEntity(AgenceVoyageDTO agenceVoyageDTO){
        if (agenceVoyageDTO == null){
            return null;
        }
        return AgenceVoyage.builder()
                .id(agenceVoyageDTO.getId())
                .nom(agenceVoyageDTO.getNom())
                .objectif(agenceVoyageDTO.getObjectif())
                .build();
    }
}
