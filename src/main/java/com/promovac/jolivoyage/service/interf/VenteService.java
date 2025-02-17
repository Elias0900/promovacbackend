package com.promovac.jolivoyage.service.interf;

import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.Vente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VenteService {

    /**
     * Récupère toutes les ventes réalisées.
     *
     * @return Liste de tous les DTOs de ventes.
     */
    List<VenteDto> findAll();

    /**
     * Sauvegarde ou met à jour une vente.
     *
     * @param venteDto Le DTO représentant la vente à sauvegarder ou à mettre à jour.
     * @return Le DTO de la vente sauvegardée ou mise à jour.
     */
    VenteDto saveOrUpdateVente(VenteDto venteDto);

    /**
     * Récupère toutes les ventes d'un utilisateur spécifique en fonction de son ID.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Liste des ventes de l'utilisateur sous forme de DTOs.
     */
    List<VenteDto> findVentesByUserId(Long userId);

    /**
     * Calcule le montant total des assurances souscrites par un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Le montant total des assurances souscrites par l'utilisateur.
     */
    Double totalMontantAssuranceByUserId(Long userId);

    /**
     * Calcule le montant total des assurances souscrites par un utilisateur,
     * dont le tour opérateur contient une chaîne spécifique.
     *
     * @param userId L'ID de l'utilisateur.
     * @param tourOperateur Le nom du tour opérateur à filtrer.
     * @return Le montant total des assurances souscrites par l'utilisateur filtré par tour opérateur.
     */
    Double totalMontantTOByUserIdForFramContaining(Long userId, String tourOperateur);

    /**
     * Calcule le montant total des ventes d'un utilisateur,
     * dont le tour opérateur ne contient pas une chaîne spécifique.
     *
     * @param userId L'ID de l'utilisateur.
     * @param tourOperateur Le nom du tour opérateur à exclure.
     * @return Le montant total des ventes sans le tour opérateur spécifié.
     */
    Double totalMontantTOByUserIdForNonFram(Long userId, String tourOperateur);

    /**
     * Récupère les ventes groupées par jour pour une période donnée.
     *
     * @param start La date de début de la période.
     * @param end La date de fin de la période.
     * @return Liste des ventes groupées par jour dans la période spécifiée.
     */
    List<VentesParJourDto> venteParJour(LocalDate start, LocalDate end);

    /**
     * Récupère les ventes par jour pour une période donnée et un utilisateur spécifique.
     *
     * @param start La date de début de la période.
     * @param end La date de fin de la période.
     * @param user L'ID de l'utilisateur.
     * @return Liste des ventes groupées par jour dans la période et pour l'utilisateur spécifié.
     */
    List<VentesParJourDto> venteParJourByUser(LocalDateTime start, LocalDateTime end, Long user);

    /**
     * Récupère les ventes groupées par jour pour une année donnée.
     *
     * @param year L'année pour laquelle récupérer les ventes par jour.
     * @return Liste des ventes groupées par jour pour l'année spécifiée.
     */
    List<VentesParJourDto> getVentesParJour(int year);

    /**
     * Récupère les ventes groupées par mois pour une année donnée.
     *
     * @param year L'année pour laquelle récupérer les ventes par mois.
     * @return Liste des ventes groupées par mois pour l'année spécifiée.
     */
    List<VentesParJourDto> getVentesParMois(int year);

    /**
     * Récupère les ventes groupées par année.
     *
     * @return Liste des ventes groupées par année.
     */
    List<VentesParJourDto> getVentesParAn();

    /**
     * Calcule le montant total des assurances souscrites par tous les utilisateurs.
     *
     * @return Le montant total des assurances souscrites.
     */
    Double totalMontantAssurance();

    /**
     * Compte le nombre d'assurances souscrites par tous les utilisateurs.
     *
     * @return Le nombre total d'assurances souscrites.
     */
    Double countAssuranceSouscrite();

    /**
     * Calcule le montant total des ventes réalisées par tous les utilisateurs.
     *
     * @return Le montant total des ventes.
     */
    Double totalMontant();

    /**
     * Calcule le montant total des ventes sans assurance pour un tour opérateur spécifique.
     *
     * @param tourOperateur Le nom du tour opérateur à filtrer.
     * @return Le montant total des ventes sans assurance pour le tour opérateur spécifié.
     */
    Double totalMontantTOForFramContaining(String tourOperateur);

    /**
     * Calcule le montant total des ventes sans assurance pour un tour opérateur exclu.
     *
     * @param tourOperateur Le nom du tour opérateur à exclure.
     * @return Le montant total des ventes sans assurance excluant le tour opérateur spécifié.
     */
    Double totalMontantTOForNonFram(String tourOperateur);



    List<VenteDto> rechercherByAgence(String keyword, Long agenceId);

    List<VenteDto> rechercherByUser(String keyword, Long userId);

    List<VenteDto> getVentesDuMoisPrecedentByUser(Long userId);
}
