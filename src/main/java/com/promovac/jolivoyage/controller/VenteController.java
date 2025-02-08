package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.service.interf.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    /**
     * Sauvegarde ou met à jour une vente.
     *
     * @param vente La vente à sauvegarder ou à mettre à jour.
     * @return La vente sauvegardée ou mise à jour.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<VenteDto> saveOrUpdateVente(@RequestBody VenteDto vente) {
        try {
            // Sauvegarder ou mettre à jour la vente
            VenteDto savedVente = venteService.saveOrUpdateVente(vente);

            // Vérifier si la vente est nouvelle ou mise à jour
            if (vente.getId() == null) {
                // Si pas d'ID (nouvelle vente), retourner CREATED
                return ResponseEntity.status(HttpStatus.CREATED).body(savedVente);
            } else {
                // Si l'ID est présent, c'est une mise à jour, retourner OK
                return ResponseEntity.status(HttpStatus.OK).body(savedVente);
            }
        } catch (Exception e) {
            // Gérer les erreurs (par exemple, si les données sont invalides)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Récupère toutes les ventes d'un utilisateur par son ID.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Liste des ventes de l'utilisateur.
     */
    @GetMapping(path = "/user/{userId}", produces = "application/json")
    public ResponseEntity<List<VenteDto>> getVentesByUserId(@PathVariable Long userId) {
        List<VenteDto> ventes = venteService.findVentesByUserId(userId);
        if (ventes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne un 204 si aucune vente n'est trouvée
        }
        return ResponseEntity.ok(ventes);
    }

    /**
     * Calcule le montant total des assurances pour un utilisateur.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Montant total des assurances.
     */
    @GetMapping(path = "/user/{userId}/total-assurance", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantAssuranceByUserId(@PathVariable Long userId) {
        Double totalAssurance = venteService.totalMontantAssuranceByUserId(userId);
        return ResponseEntity.ok(totalAssurance);
    }

    /**
     * Calcule le montant total des TO pour un utilisateur dont le tour opérateur contient une chaîne spécifique.
     *
     * @param userId ID de l'utilisateur.
     * @return Le montant total des ventes.
     */
    @GetMapping(path = "/montantTOByUser/{userId}/fram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOByUserForFram(
            @PathVariable Long userId) {
        try {
            Double totalMontant = venteService.totalMontantTOByUserIdForFramContaining(userId, "FRAM");
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Calcule le montant total des ventes pour un utilisateur dont le tour opérateur contient "FRAM".
     *
     * @param userId ID de l'utilisateur.
     * @return Le montant total des ventes.
     */
    @GetMapping(path = "/montantTOByUser/{userId}/nofram", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantTOByUserForNonFram(
            @PathVariable Long userId) {
        try {
            Double totalMontant = venteService.totalMontantTOByUserIdForNonFram(userId, "FRAM");
            return ResponseEntity.ok(totalMontant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Endpoint pour récupérer les ventes par jour entre deux dates.
     *
     * @param startDate Date de début (format : yyyy-MM-dd).
     * @param endDate   Date de fin (format : yyyy-MM-dd).
     * @return Liste des ventes par jour.
     */
    @GetMapping("/par-jour")
    public ResponseEntity<List<VentesParJourDto>> getVentesParJour(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<VentesParJourDto> ventesParJour = venteService.venteParJour(startDate, endDate);

        return ResponseEntity.ok(ventesParJour);
    }


    /**
     * Endpoint pour récupérer les ventes par jour entre deux dates pour un employé.
     *
     * @param startDate Date de début (format : yyyy-MM-dd).
     * @param endDate   Date de fin (format : yyyy-MM-dd).
     * @return Liste des ventes par jour.
     */
    @GetMapping(path = "/par-jour/{userId}", produces = "application/json")
    public ResponseEntity<List<VentesParJourDto>> getVentesParJourByUser(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<VentesParJourDto> ventesParJour = venteService.venteParJourByUser(startDate, endDate, userId);

        return ResponseEntity.ok(ventesParJour);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<VenteDto>> getAllVentes(){
        return ResponseEntity.ok().body(venteService.findAll());
    }

    @GetMapping(path = "/par-jour-charts/{year}", produces = "application/json")
    public List<VentesParJourDto> getVentesParJourCharts(@PathVariable int year) {
        return venteService.getVentesParJour(year);
    }

    @GetMapping(path = "/par-mois-charts/{year}", produces = "application/json")
    public List<VentesParJourDto> getVentesParMoisCharts(@PathVariable int year) {
        return venteService.getVentesParMois(year);
    }

    @GetMapping(path = "/par-an", produces = "application/json")
    public List<VentesParJourDto> getVentesParAn() {
        return venteService.getVentesParAn();
    }

    /**
     * Calcule le montant total des assurances pour toutes les ventes.
     *
     * @return Montant total des assurances pour toutes les ventes.
     */
    @GetMapping(path = "/total-assurance", produces = "application/json")
    public ResponseEntity<Double> getTotalMontantAssurance() {
        Double totalAssurance = venteService.totalMontantAssurance();
        return ResponseEntity.ok(totalAssurance);
    }

    /**
     * Calcule le nombre total d'assurances souscrites pour toutes les ventes.
     *
     * @return Le nombre total d'assurances souscrites.
     */
    @GetMapping(path = "/total-assurance-souscrite", produces = "application/json")
    public ResponseEntity<Double> getCountAssuranceSouscrite() {
        Double countAssurance = venteService.countAssuranceSouscrite();
        return ResponseEntity.ok(countAssurance);
    }

    /**
     * Calcule le montant total des ventes pour toutes les ventes.
     *
     * @return Le montant total des ventes.
     */
    @GetMapping(path = "/total-montant", produces = "application/json")
    public ResponseEntity<Double> getTotalMontant() {
        Double totalMontant = venteService.totalMontant();
        return ResponseEntity.ok(totalMontant);
    }

    /**
     * Calcule le montant total des ventes pour toutes les ventes d'un tour opérateur spécifique (par exemple "FRAM").
     *
     * @param tourOperateur Le tour opérateur à filtrer.
     * @return Le montant total des ventes pour ce tour opérateur.
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
     * Calcule le montant total des ventes pour toutes les ventes d'un tour opérateur spécifique, excluant un tour opérateur donné.
     *
     * @param tourOperateur Le tour opérateur à exclure.
     * @return Le montant total des ventes pour les autres tours opérateurs.
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
}
