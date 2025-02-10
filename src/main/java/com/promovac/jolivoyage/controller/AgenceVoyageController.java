package com.promovac.jolivoyage.controller;

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


    @PatchMapping("/{id}/objectif")
    public ResponseEntity<?> updateObjectif(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double nouvelObjectif = request.get("objectif"); // Récupérer l’objectif envoyé

        AgenceVoyage agence = agenceVoyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID : " + id));

        agence.setObjectif(nouvelObjectif); // Modifier l'objectif
        agenceVoyageRepository.save(agence); // Sauvegarder en base

        return ResponseEntity.ok(agence);
    }

    @GetMapping(value = "/{agenceId}/users", produces = "application/json")
    public ResponseEntity<List<User>> getUsersByAgence(@PathVariable Long agenceId) {
        List<User> users = agenceVoyageService.getUsersByAgenceId(agenceId);
        return ResponseEntity.ok(users);
    }

}
