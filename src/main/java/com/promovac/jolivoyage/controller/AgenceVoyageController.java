package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.AgenceVoyageDTO;
import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.AgenceVoyageRepository;
import com.promovac.jolivoyage.service.interf.AgenceVoyageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/agence")
@Slf4j
public class AgenceVoyageController {
    private final AgenceVoyageRepository agenceVoyageRepository;
    private final AgenceVoyageService agenceVoyageService;

    /**
     * Mise à jour de l'objectif d'une agence.
     * Cette méthode permet de modifier l'objectif d'une agence existante en fonction de son ID.
     *
     * @param id L'ID de l'agence à mettre à jour.
     * @param request Map contenant la nouvelle valeur de l'objectif.
     * @return La réponse HTTP contenant l'agence mise à jour.
     */
    @PatchMapping("/{id}/objectif")
    public ResponseEntity<?> updateObjectif(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double nouvelObjectif = request.get("objectif");

        AgenceVoyage agence = agenceVoyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID : " + id));

        agence.setObjectif(nouvelObjectif);
        agenceVoyageRepository.save(agence);

        // Retourne le DTO au lieu de l'entité complète
        return ResponseEntity.ok(AgenceVoyageDTO.mapToDTO(agence)
        );
    }

    @GetMapping(value = "/{agenceId}/users", produces = "application/json")
    public ResponseEntity<List<User>> getUsersByAgence(@PathVariable Long agenceId) {
        List<User> users = agenceVoyageService.getUsersByAgenceId(agenceId);
        return ResponseEntity.ok(users);
    }

}
