package com.promovac.jolivoyage.service.interf;


import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VenteService {

    /**
     * Sauvegarde ou met à jour une vente.
     *
     * @param venteDto Le DTO de la vente à sauvegarder ou à mettre à jour.
     * @return Le DTO de la vente sauvegardée ou mise à jour.
     */
    VenteDto saveOrUpdateVente(VenteDto venteDto);

    /**
     * Récupère toutes les ventes d'un utilisateur par son ID.
     *
     * @param userId ID de l'utilisateur.
     * @return Liste des ventes de l'utilisateur sous forme de DTOs.
     */
    List<VenteDto> findVentesByUserId(Long userId);

    /**
     * Calcule le montant total des assurances pour un utilisateur.
     *
     * @param userId ID de l'utilisateur.
     * @return Montant total des assurances.
     */
    Double totalMontantAssuranceByUserId(Long userId);

    /**
     * Calcule le montant total des assurances pour un utilisateur
     * dont le tour opérateur contient une chaîne spécifique.
     *
     * @param userId ID de l'utilisateur.
     * @param tourOperateur Filtre sur le nom du tour opérateur.
     * @return Montant total des assurances filtrées.
     */
    Double totalMontantTOByUserIdForFramContaining(Long userId, String tourOperateur);

    /**
     * Calcule le montant total des TO pour un utilisateur
     * dont le tour opérateur ne contient pas une chaîne spécifique.
     *
     * @param userId ID de l'utilisateur.
     * @param tourOperateur Filtre sur le nom du tour opérateur.
     * @return Montant total des TO pour les autres opérateurs.
     */
    Double totalMontantTOByUserIdForNonFram(Long userId, String tourOperateur);

    List<VentesParJourDto> venteParJour(LocalDateTime start, LocalDateTime end);
    List<VentesParJourDto> venteParJourByUser(LocalDateTime start, LocalDateTime end, Long user);
}
