package com.promovac.jolivoyage.service;


import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.entity.Vente;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.repository.VenteRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import com.promovac.jolivoyage.service.interf.VenteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VenteServiceImpl implements VenteService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private UserRepository myAppUserRepository;

    @Autowired
    private BilanService bilanService;

    @Override
    public VenteDto saveOrUpdateVente(VenteDto venteDto) {
        // Charger l'utilisateur depuis la base de données
        User user = myAppUserRepository.findById(venteDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec l'ID : " + venteDto.getUserId()));

        // Convertir le DTO en entité
        Vente vente = VenteDto.toEntity(venteDto);
        vente.setUser(user); // Associer l'utilisateur chargé
        vente.setTotalSansAssurance(vente.getVenteTotal().doubleValue() - vente.getMontantAssurance());

        // Sauvegarder la vente
        Vente savedVente = venteRepository.save(vente);

        bilanService.saveOrUpdateBilan(user.getId());
        // Retourner le DTO
        return VenteDto.fromEntity(savedVente);
    }


    @Override
    public List<VenteDto> findVentesByUserId(Long userId) {
        List<Vente> ventes = venteRepository.findVentesByUserId(userId); // Récupération des ventes
        return ventes.stream()
                .map(VenteDto::fromEntity) // Conversion Entités -> DTOs
                .toList();
    }

    @Override
    public Double totalMontantAssuranceByUserId(Long userId) {
        return venteRepository.totalMontantAssuranceByUserId(userId);
    }

    @Override
    public Double totalMontantTOByUserIdForFramContaining(Long userId, String tourOperateur) {
        return venteRepository.totalMontantTOByUserIdForFramContaining(userId, "%" + "FRAM" + "%");

    }

    @Override
    public Double totalMontantTOByUserIdForNonFram(Long userId, String tourOperateur) {
        return venteRepository.totalMontantTOByUserIdForNonFram(userId, "%" + "FRAM" + "%");

    }

    @Override
    public List<VentesParJourDto> venteParJour(LocalDateTime startDate, LocalDateTime endDate) {
        // Convertir LocalDateTime en LocalDate pour que le type corresponde avec la base de données
        LocalDate start = startDate.toLocalDate(); // Juste la date sans l'heure
        LocalDate end = endDate.toLocalDate(); // Juste la date sans l'heure

        // Appel de la méthode du repository avec les dates ajustées
        return venteRepository.findVentesParJour(start, end);
    }
    @Override
    public List<VentesParJourDto> venteParJourByUser(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        // Convertir LocalDateTime en LocalDate pour que le type corresponde avec la base de données
        LocalDate start = startDate.toLocalDate(); // Juste la date sans l'heure
        LocalDate end = endDate.toLocalDate(); // Juste la date sans l'heure

        // Appel de la méthode du repository avec les dates ajustées
        return venteRepository.findVentesParJourByUser(start, end, userId);
    }


}
