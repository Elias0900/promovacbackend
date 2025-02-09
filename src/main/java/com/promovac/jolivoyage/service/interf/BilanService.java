package com.promovac.jolivoyage.service.interf;


import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.entity.Bilan;

import java.util.List;
import java.util.Map;

public interface BilanService {

    /**
     * Créer ou mettre à jour un Bilan.
     *
     * @param userId L'Bilan à créer ou mettre à jour.
     * @return L'Bilan créé ou mis à jour.
     */
    BilanDto saveOrUpdateBilan(Long userId);

    /**
     * Obtenir un Bilan par ID.
     *
     * @param id L'ID de l'Bilan.
     * @return L'Bilan correspondant à l'ID.
     */
    BilanDto getBilanById(Long id);

    /**
     * Supprimer un Bilan par ID.
     *
     * @param id L'ID de l'Bilan à supprimer.
     */
    void deleteBilan(Long id);

    BilanDto getBilanByUserId(Long id);

    Map<String, Object> getBilanByAgence(Long agenceId);
}
