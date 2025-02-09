package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.repository.AgenceVoyageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/agence")
@Slf4j
public class AgenceVoyagecontroller {
    private final AgenceVoyageRepository agenceVoyageRepository;

    @PatchMapping("/{id}/objectif")
    public ResponseEntity<?> updateObjectif(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double nouvelObjectif = request.get("objectif"); // Récupérer l’objectif envoyé

        AgenceVoyage agence = agenceVoyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID : " + id));

        agence.setObjectif(nouvelObjectif); // Modifier l'objectif
        agenceVoyageRepository.save(agence); // Sauvegarder en base

        return ResponseEntity.ok(agence);
    }


}
