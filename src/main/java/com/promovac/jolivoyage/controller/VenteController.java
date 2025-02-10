package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.Vente;
import com.promovac.jolivoyage.service.interf.VenteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventes")
public class VenteController {

    private final VenteService venteService;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    /**
     * Sauvegarde ou met à jour une vente.
     * Cette méthode reçoit une vente via une requête POST et détermine si elle doit être sauvegardée ou mise à jour en fonction de la présence d'un ID dans l'objet.
     *
     * @param vente L'objet VenteDto à sauvegarder ou à mettre à jour.
     * @return La vente sauvegardée ou mise à jour sous forme de VenteDto avec le statut HTTP approprié.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<VenteDto> saveOrUpdateVente(@RequestBody VenteDto vente) {
        try {
            VenteDto savedVente = venteService.saveOrUpdateVente(vente);
            if (vente.getId() == null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedVente);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(savedVente);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Récupère toutes les ventes d'un utilisateur par son ID.
     * Cette méthode permet de récupérer toutes les ventes liées à un utilisateur spécifique à partir de son ID.
     *
     * @param userId L'ID de l'utilisateur pour lequel on souhaite récupérer les ventes.
     * @return Une liste de VenteDto représentant les ventes de l'utilisateur.
     *         Retourne un statut 204 No Content si aucune vente n'est trouvée.
     */
    @GetMapping(path = "/user/{userId}", produces = "application/json")
    public ResponseEntity<List<VenteDto>> getVentesByUserId(@PathVariable Long userId) {
        List<VenteDto> ventes = venteService.findVentesByUserId(userId);
        if (ventes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventes);
    }

    /**
     * Calcule le montant total des assurances souscrites par un utilisateur.
     * Cette méthode retourne le montant total des assurances souscrites par un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur pour lequel on souhaite calculer le total des assurances.
     * @return Le montant total des assurances sous forme de valeur Double.
     */
    @GetMapping(path = "/user/{userId}/total-assurance", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantAssuranceByUserId(@PathVariable Long userId) {
        Double totalAssurance = venteService.totalMontantAssuranceByUserId(userId);
        return ResponseEntity.ok(totalAssurance);
    }

    /**
     * Calcule le montant total des ventes pour un utilisateur ayant un tour opérateur contenant "FRAM".
     * Cette méthode permet de filtrer les ventes de l'utilisateur qui sont liées à un tour opérateur spécifique.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Le montant total des ventes pour un tour opérateur contenant "FRAM".
     */
    @GetMapping(path = "/montantTOByUser/{userId}/fram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOByUserForFram(@PathVariable Long userId) {
        try {
            Double totalMontant = venteService.totalMontantTOByUserIdForFramContaining(userId, "FRAM");
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Calcule le montant total des ventes pour un utilisateur sans prendre en compte les ventes avec le tour opérateur "FRAM".
     * Cette méthode est similaire à la précédente mais exclut spécifiquement les ventes contenant "FRAM".
     *
     * @param userId L'ID de l'utilisateur.
     * @return Le montant total des ventes sans les ventes "FRAM".
     */
    @GetMapping(path = "/montantTOByUser/{userId}/nofram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOByUserForNonFram(@PathVariable Long userId) {
        try {
            Double totalMontant = venteService.totalMontantTOByUserIdForNonFram(userId, "FRAM");
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Récupère les ventes par jour entre deux dates spécifiées.
     * Cette méthode renvoie une liste des ventes groupées par jour pour une période donnée.
     *
     * @param startDate La date de début pour la période.
     * @param endDate La date de fin pour la période.
     * @return Une liste des ventes par jour sous forme de VentesParJourDto.
     */
    @GetMapping("/par-jour")
    public ResponseEntity<List<VentesParJourDto>> getVentesParJour(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<VentesParJourDto> ventesParJour = venteService.venteParJour(startDate, endDate);
        return ResponseEntity.ok(ventesParJour);
    }

    /**
     * Récupère les ventes par jour entre deux dates pour un utilisateur spécifique.
     * Cette méthode permet de récupérer les ventes d'un utilisateur sur une période donnée, groupées par jour.
     *
     * @param userId L'ID de l'utilisateur.
     * @param startDate La date de début de la période.
     * @param endDate La date de fin de la période.
     * @return Une liste des ventes par jour pour l'utilisateur.
     */
    @GetMapping(path = "/par-jour/{userId}", produces = "application/json")
    public ResponseEntity<List<VentesParJourDto>> getVentesParJourByUser(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<VentesParJourDto> ventesParJour = venteService.venteParJourByUser(startDate, endDate, userId);
        return ResponseEntity.ok(ventesParJour);
    }

    /**
     * Récupère toutes les ventes.
     * Cette méthode permet de récupérer l'ensemble des ventes présentes dans la base de données.
     *
     * @return Une liste de toutes les ventes sous forme de VenteDto.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<VenteDto>> getAllVentes(){
        return ResponseEntity.ok().body(venteService.findAll());
    }

    /**
     * Récupère les ventes par jour sous forme de graphique pour une année spécifique.
     * Cette méthode permet d'obtenir les ventes groupées par jour pour une année donnée.
     *
     * @param year L'année pour laquelle on souhaite obtenir les ventes par jour.
     * @return Une liste de ventes par jour pour l'année donnée sous forme de VentesParJourDto.
     */
    @GetMapping(path = "/par-jour-charts/{year}", produces = "application/json")
    public List<VentesParJourDto> getVentesParJourCharts(@PathVariable int year) {
        return venteService.getVentesParJour(year);
    }

    /**
     * Récupère les ventes par mois sous forme de graphique pour une année spécifique.
     * Cette méthode permet d'obtenir les ventes groupées par mois pour une année donnée.
     *
     * @param year L'année pour laquelle on souhaite obtenir les ventes par mois.
     * @return Une liste des ventes groupées par mois pour l'année donnée.
     */
    @GetMapping(path = "/par-mois-charts/{year}", produces = "application/json")
    public List<VentesParJourDto> getVentesParMoisCharts(@PathVariable int year) {
        return venteService.getVentesParMois(year);
    }

    /**
     * Récupère les ventes groupées par année.
     * Cette méthode permet d'obtenir le montant des ventes pour chaque année.
     *
     * @return Une liste de ventes groupées par année sous forme de VentesParJourDto.
     */
    @GetMapping(path = "/par-an", produces = "application/json")
    public List<VentesParJourDto> getVentesParAn() {
        return venteService.getVentesParAn();
    }

    /**
     * Calcule le montant total des assurances pour toutes les ventes.
     * Cette méthode calcule la somme totale des assurances souscrites pour toutes les ventes.
     *
     * @return Le montant total des assurances sous forme de Double.
     */
    @GetMapping(path = "/total-assurance", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantAssurance() {
        Double totalAssurance = venteService.totalMontantAssurance();
        return ResponseEntity.ok(totalAssurance);
    }

    /**
     * Calcule le nombre total d'assurances souscrites pour toutes les ventes.
     * Cette méthode retourne le nombre total d'assurances souscrites pour toutes les ventes dans le système.
     *
     * @return Le nombre total d'assurances sous forme de Double.
     */
    @GetMapping(path = "/total-assurance-souscrite", produces = "application/json")
    public ResponseEntity<Double> getCountAssuranceSouscrite() {
        Double countAssurance = venteService.countAssuranceSouscrite();
        return ResponseEntity.ok(countAssurance);
    }

    /**
     * Calcule le montant total des ventes pour toutes les ventes.
     * Cette méthode retourne le montant total de toutes les ventes dans le système.
     *
     * @return Le montant total des ventes sous forme de Double.
     */
    @GetMapping(path = "/total-montant", produces = "application/json")
    public ResponseEntity<Double> getTotalMontant() {
        Double totalMontant = venteService.totalMontant();
        return ResponseEntity.ok(totalMontant);
    }

    /**
     * Calcule le montant total des ventes pour toutes les ventes d'un tour opérateur spécifique.
     * Cette méthode permet de filtrer les ventes en fonction d'un tour opérateur spécifique (ex. "FRAM").
     *
     * @param tourOperateur Le tour opérateur dont on veut calculer le montant total des ventes.
     * @return Le montant total des ventes pour ce tour opérateur sous forme de Double.
     */
    @GetMapping(path = "/montantTO/{tourOperateur}/fram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOForFram(
            @PathVariable String tourOperateur) {
        try {
            Double totalMontant = venteService.totalMontantTOForFramContaining(tourOperateur);
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Calcule le montant total des ventes pour toutes les ventes d'un tour opérateur spécifique, en excluant un tour opérateur donné.
     * Cette méthode permet de calculer le montant des ventes en excluant un tour opérateur spécifié.
     *
     * @param tourOperateur Le tour opérateur à exclure des calculs.
     * @return Le montant total des ventes pour les autres tours opérateurs sous forme de Double.
     */
    @GetMapping(path = "/montantTO/{tourOperateur}/nofram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOForNonFram(
            @PathVariable String tourOperateur) {
        try {
            Double totalMontant = venteService.totalMontantTOForNonFram(tourOperateur);
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Recherche des ventes pour un utilisateur avec différents critères de filtrage.
     * Cette méthode permet de rechercher des ventes en filtrant par différents critères tels que le nom, le prénom, le numéro de dossier, etc.
     *
     * @param userId L'ID de l'utilisateur pour lequel on recherche les ventes.
     * @param nom Le nom de la personne (optionnel).
     * @param prenom Le prénom de la personne (optionnel).
     * @param numeroDossier Le numéro de dossier des ventes (optionnel).
     * @param dateDepart La date de départ des ventes (optionnel).
     * @param dateValidation La date de validation des ventes (optionnel).
     * @param assurance Indicateur si une assurance a été souscrite (optionnel).
     * @param sortBy Le champ par lequel trier (optionnel, par défaut "numeroDossier").
     * @param sortDirection Le sens du tri (optionnel, par défaut "ASC").
     * @return Une liste de ventes correspondant aux critères de recherche.
     */
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Vente>> searchVentes(
            @PathVariable Long userId,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String numeroDossier,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDepart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateValidation,
            @RequestParam(required = false) Boolean assurance,
            @RequestParam(defaultValue = "numeroDossier") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.ok().body(venteService.searchVentes(userId, nom, prenom, numeroDossier, dateDepart, dateValidation, assurance, sortBy, sortDirection));
    }

    /**
     * Recherche des ventes pour une agence avec différents critères de filtrage.
     * Cette méthode permet de rechercher des ventes dans une agence spécifique avec des critères filtrés (similaire à la méthode précédente mais pour une agence).
     *
     * @param agenceId L'ID de l'agence pour laquelle on recherche les ventes.
     * @param userId L'ID de l'utilisateur associé aux ventes (optionnel).
     * @param nomUser Le nom de l'utilisateur (optionnel).
     * @param prenomUser Le prénom de l'utilisateur (optionnel).
     * @param numeroDossier Le numéro de dossier des ventes (optionnel).
     * @param dateDepart La date de départ des ventes (optionnel).
     * @param dateValidation La date de validation des ventes (optionnel).
     * @param assurance Indicateur si une assurance a été souscrite (optionnel).
     * @param sortBy Le champ par lequel trier (optionnel).
     * @param sortDirection Le sens du tri (optionnel).
     * @return Une liste de ventes pour l'agence correspondant aux critères de recherche.
     */
    @GetMapping("/agence/{agenceId}/search")
    public ResponseEntity<List<Vente>> searchVentesByAgence(
            @PathVariable Long agenceId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String nomUser,
            @RequestParam(required = false) String prenomUser,
            @RequestParam(required = false) String numeroDossier,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDepart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateValidation,
            @RequestParam(required = false) Boolean assurance,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.ok().body(venteService.searchVentesByAgence(agenceId, userId, nomUser, prenomUser, numeroDossier, dateDepart, dateValidation, assurance, sortBy, sortDirection));
    }
}
