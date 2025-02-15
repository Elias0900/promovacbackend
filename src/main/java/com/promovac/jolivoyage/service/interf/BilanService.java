package com.promovac.jolivoyage.service.interf;

import com.promovac.jolivoyage.dto.BilanDto;

import java.time.YearMonth;
import java.util.Map;

public interface BilanService {

    /**
     * Crée ou met à jour un Bilan pour un utilisateur donné.
     *
     * @param userId L'ID de l'utilisateur pour lequel le bilan doit être créé ou mis à jour.
     */
    void saveOrUpdateBilan(Long userId, YearMonth moisActuel);

    /**
     * Récupère un Bilan par son ID.
     *
     * @param id L'ID du Bilan à récupérer.
     * @return Le Bilan correspondant à l'ID, sous forme de DTO.
     */
    BilanDto getBilanById(Long id);

    /**
     * Supprime un Bilan par son ID.
     *
     * @param id L'ID du Bilan à supprimer.
     */
    void deleteBilan(Long id);

    /**
     * Récupère un Bilan pour un utilisateur spécifique, basé sur l'ID de l'utilisateur.
     *
     * @param id L'ID de l'utilisateur pour lequel récupérer le Bilan.
     * @return Le Bilan de l'utilisateur spécifié, sous forme de DTO.
     */
    BilanDto getBilanByUserId(Long id);

    /**
     * Récupère un Bilan par ID d'agence.
     *
     * @param agenceId L'ID de l'agence pour laquelle récupérer le Bilan.
     * @return Un Map contenant des informations détaillées sur le Bilan pour l'agence spécifiée.
     */
    Map<String, Object> getBilanByAgence(Long agenceId);

    BilanDto getBilanByMoisAndUserId(Long userId, YearMonth choisirMois);
}
