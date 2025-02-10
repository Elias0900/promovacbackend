package com.promovac.jolivoyage.service;


import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.entity.Vente;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.repository.VenteRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import com.promovac.jolivoyage.service.interf.VenteService;
import com.promovac.jolivoyage.specifications.VentesSpecificationsByAgence;
import com.promovac.jolivoyage.specifications.VentesSpecificationsByUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<VenteDto> findAll() {
       List<Vente> venteList = venteRepository.findAll();
       List<VenteDto> venteDtoList = new ArrayList<>();


        venteList.forEach(vente -> {
          VenteDto venteDto = VenteDto.fromEntity(vente);
            venteDtoList.add(venteDto);
       });

        return venteDtoList;

    }

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
    public List<VentesParJourDto> venteParJour(LocalDate startDate, LocalDate endDate) {
        // Appel de la méthode du repository avec les dates ajustées
        return venteRepository.findVentesParJour(startDate, endDate);
    }
    @Override
    public List<VentesParJourDto> venteParJourByUser(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        // Convertir LocalDateTime en LocalDate pour que le type corresponde avec la base de données
        LocalDate start = startDate.toLocalDate(); // Juste la date sans l'heure
        LocalDate end = endDate.toLocalDate(); // Juste la date sans l'heure

        // Appel de la méthode du repository avec les dates ajustées
        return venteRepository.findVentesParJourByUser(start, end, userId);
    }

    @Override
    public List<VentesParJourDto> getVentesParJour(int year) {
        return venteRepository.findVentesParJourGraph(year);
    }

    @Override
    public List<VentesParJourDto> getVentesParMois(int year) {
        return venteRepository.findVentesParMoisGraph(year);
    }

    @Override
    public List<VentesParJourDto> getVentesParAn() {
        return venteRepository.findVentesParAn();
    }

    @Override
    public Double totalMontantAssurance() {
        return venteRepository.totalMontantAssurance();
    }

    @Override
    public Double countAssuranceSouscrite() {
        return venteRepository.countAssuranceSouscrite();
    }

    @Override
    public Double totalMontant() {
        return venteRepository.totalMontant();
    }

    @Override
    public Double totalMontantTOForFramContaining(String tourOperateur) {
        return venteRepository.totalMontantTOForFramContaining(tourOperateur);
    }

    @Override
    public Double totalMontantTOForNonFram(String tourOperateur) {
        return venteRepository.totalMontantTOForNonFram(tourOperateur);
    }

    @Override
    public List<Vente> searchVentes(Long userId, String nom, String prenom, String numeroDossier,
                                    LocalDate dateDepart, LocalDate dateValidation, Boolean assurance,
                                    String sortBy, String sortDirection) {

        Specification<Vente> spec = Specification
                .where(VentesSpecificationsByUser.hasUserId(userId))
                .and(VentesSpecificationsByUser.hasNom(nom))
                .and(VentesSpecificationsByUser.hasPrenom(prenom))
                .and(VentesSpecificationsByUser.hasNumeroDossier(numeroDossier))
                .and(VentesSpecificationsByUser.hasDateDepartAfter(dateDepart))
                .and(VentesSpecificationsByUser.hasDateValidation(dateValidation))
                .and(VentesSpecificationsByUser.hasAssurance(assurance));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return venteRepository.findAll(spec, sort);
    }

    @Override
    public List<Vente> searchVentesByAgence(Long agenceId, Long userId, String nomUser, String prenomUser,
                                            String numeroDossier, LocalDate dateDepart, LocalDate dateValidation,
                                            Boolean assurance, String sortBy, String sortDirection) {

        Specification<Vente> spec = Specification
                .where(VentesSpecificationsByAgence.hasAgenceId(agenceId))
                .and(VentesSpecificationsByAgence.hasUserId(userId))
                .and(VentesSpecificationsByAgence.hasNomUser(nomUser))
                .and(VentesSpecificationsByAgence.hasPrenomUser(prenomUser))
                .and(VentesSpecificationsByAgence.hasNumeroDossier(numeroDossier))
                .and(VentesSpecificationsByAgence.hasDateDepartAfter(dateDepart))
                .and(VentesSpecificationsByAgence.hasDateValidation(dateValidation))
                .and(VentesSpecificationsByAgence.hasAssurance(assurance));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return venteRepository.findAll(spec, sort);
    }
}
