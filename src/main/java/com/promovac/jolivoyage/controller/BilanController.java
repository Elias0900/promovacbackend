package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.entity.Bilan;
import com.promovac.jolivoyage.service.interf.BilanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bilans")
public class BilanController {

    @Autowired
    private BilanService bilanService;

    /**
     * Sauvegarde ou met à jour un bilan pour un utilisateur donné.
     *
     * @param userId L'ID de l'utilisateur pour lequel le bilan doit être créé ou mis à jour.
     * @return Le bilan sauvegardé ou mis à jour.
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<BilanDto> saveOrUpdateBilan(@PathVariable Long userId) {
        BilanDto bilanDto = bilanService.saveOrUpdateBilan(userId);
        return ResponseEntity.ok(bilanDto);
    }

    /**
     * Récupère un bilan par son ID.
     *
     * @param id L'ID du bilan à récupérer.
     * @return Le bilan correspondant à l'ID fourni.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BilanDto> getBilanById(@PathVariable Long id) {
        BilanDto bilanDto = bilanService.getBilanById(id);
        return ResponseEntity.ok(bilanDto);
    }

    /**
     * Récupère un bilan par l'ID de l'utilisateur.
     *
     * @param id L'ID de l'utilisateur pour lequel le bilan doit être récupéré.
     * @return Le bilan de l'utilisateur.
     */
    @GetMapping(path = "/user/{id}", produces = "application/json")
    public ResponseEntity<BilanDto> getBilanByUserId(@PathVariable Long id) {
        BilanDto bilanDto = bilanService.getBilanByUserId(id);
        return ResponseEntity.ok(bilanDto);
    }

    /**
     * Supprime un bilan par son ID.
     *
     * @param id L'ID du bilan à supprimer.
     * @return Une réponse HTTP vide avec un statut 204 No Content si la suppression a réussi.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilan(@PathVariable Long id) {
        bilanService.deleteBilan(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère les bilans d'une agence donnée.
     *
     * @param agenceId L'ID de l'agence pour laquelle le bilan doit être récupéré.
     * @return Un objet contenant les bilans de l'agence.
     */
    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<Map<String, Object>> getBilanByAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(bilanService.getBilanByAgence(agenceId));
    }
}
