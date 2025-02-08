package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.service.interf.BilanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bilans")
public class BilanController {

    @Autowired
    private BilanService bilanService;

    // Endpoint pour sauvegarder ou mettre à jour un bilan
    @PostMapping("/user/{userId}")
    public ResponseEntity<BilanDto> saveOrUpdateBilan(@PathVariable Long userId) {
        BilanDto bilanDto = bilanService.saveOrUpdateBilan(userId);
        return ResponseEntity.ok(bilanDto);
    }

    // Endpoint pour récupérer un bilan par ID
    @GetMapping("/{id}")
    public ResponseEntity<BilanDto> getBilanById(@PathVariable Long id) {
        BilanDto bilanDto = bilanService.getBilanById(id);
        return ResponseEntity.ok(bilanDto);
    }

    // Endpoint pour récupérer un bilan par ID
    @GetMapping(path = "/user/{id}", produces = "application/json")
    public ResponseEntity<BilanDto> getBilanByUserId(@PathVariable Long id) {
        BilanDto bilanDto = bilanService.getBilanByUserId(id);
        return ResponseEntity.ok(bilanDto);
    }

    // Endpoint pour supprimer un bilan par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilan(@PathVariable Long id) {
        bilanService.deleteBilan(id);
        return ResponseEntity.noContent().build();
    }
}
