package com.promovac.jolivoyage.service;


import com.promovac.jolivoyage.dto.VenteDto;
import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.entity.Vente;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.repository.VenteRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import com.promovac.jolivoyage.service.interf.VenteService;
import com.promovac.jolivoyage.specifications.VentesSpecificationsByUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenteServiceImpl implements VenteService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private UserRepository myAppUserRepository;

    @Autowired
    private BilanService bilanService;

    private YearMonth moisActuel = YearMonth.now();

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
        YearMonth moisActuel = YearMonth.now();
        // Charger l'utilisateur depuis la base de données
        User user = myAppUserRepository.findById(venteDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec l'ID : " + venteDto.getUserId()));

        YearMonth testmois = YearMonth.now();

        // Convertir le DTO en entité
        Vente vente = VenteDto.toEntity(venteDto);
        vente.setUser(user); // Associer l'utilisateur chargé
        vente.setTotalSansAssurance(vente.getVenteTotal().doubleValue() - vente.getMontantAssurance());
        vente.setTransactionDate(moisActuel);

        // Sauvegarder la vente
        Vente savedVente = venteRepository.save(vente);
        bilanService.saveOrUpdateBilan(user.getId(), moisActuel);


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
        return venteRepository.totalMontantAssuranceByUserId(userId, moisActuel);
    }

    @Override
    public Double totalMontantTOByUserIdForFramContaining(Long userId, String tourOperateur) {
        return venteRepository.totalMontantTOByUserIdForFramContaining(userId, "%" + "FRAM" + "%", moisActuel);

    }

    @Override
    public Double totalMontantTOByUserIdForNonFram(Long userId, String tourOperateur) {
        return venteRepository.totalMontantTOByUserIdForNonFram(userId, "%" + "FRAM" + "%", moisActuel);

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


    /**
     * Recherche les ventes en fonction d'un mot-clé sur tous les champs sauf les dates.
     *
     * @param keyword Le mot-clé à rechercher.
     * @return Liste des ventes correspondant au critère de recherche.
     */
    @Override
    public List<VenteDto> rechercher(String keyword, Long agenceId) {
        List<Vente> ventes = venteRepository.findVentesByAgenceId(agenceId);

        return ventes.stream()
                .filter(vente ->
                        (vente.getNom() != null && vente.getNom().toLowerCase().contains(keyword.toLowerCase())) ||
                                (vente.getPrenom() != null && vente.getPrenom().toLowerCase().contains(keyword.toLowerCase())) ||
                                (vente.getNumeroDossier() != null && vente.getNumeroDossier().toLowerCase().contains(keyword.toLowerCase())) ||
                                (vente.getTourOperateur() != null && vente.getTourOperateur().toLowerCase().contains(keyword.toLowerCase())) ||
                                (vente.getVenteTotal() != null && vente.getVenteTotal().toString().contains(keyword)) ||
                                (String.valueOf(vente.getPax()).contains(keyword)) ||
                                (String.valueOf(vente.isAssurance()).contains(keyword)) ||
                                (String.valueOf(vente.getMontantAssurance()).contains(keyword)) ||
                                (String.valueOf(vente.getFraisAgence()).contains(keyword)) ||
                                (String.valueOf(vente.getTotalSansAssurance()).contains(keyword))
                )
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public List<VenteDto> getVentesDuMoisPrecedentByUser(Long userId) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        List<Vente> ventes = venteRepository.findVentesDuMoisPrecedentByUser(lastMonth, userId);
        return ventes.stream().map(VenteDto::fromEntity).collect(Collectors.toList());
    }

//    public List<VenteDto> getVentesDuMoisPrecedentByAgence(Long agenceId) {
//        LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
//        List<Vente> ventes = venteRepository.findVentesDuMoisPrecedentPourAgence(lastMonth, agenceId);
//        return ventes.stream().map(VenteDto::fromEntity).collect(Collectors.toList());
//    }

}

