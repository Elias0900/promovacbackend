package com.promovac.jolivoyage.service;


import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.entity.Bilan;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.AgenceVoyageRepository;
import com.promovac.jolivoyage.repository.BilanRepository;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.repository.VenteRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BilanServiceImpl implements BilanService {

    @Autowired
    private BilanRepository bilanRepository;

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private AgenceVoyageRepository agenceVoyageRepository;

    @Autowired
    private UserRepository myAppUserRepository;

    @Override
    public void saveOrUpdateBilan(Long userId, YearMonth mois) {
        User user = myAppUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec l'ID : " + userId));

        // Rechercher un bilan existant pour le mois donné ou en créer un nouveau
        Bilan bilan = bilanRepository.findByUserIdAndMoisBilan(userId, mois)
                .orElseGet(() -> {
                    Bilan newBilan = new Bilan();
                    newBilan.setUser(user);
                    newBilan.setMoisBilan(mois);
                    return newBilan;
                });

        // Mise à jour des valeurs du bilan avec les nouvelles ventes
        double framCroisieres = Optional.ofNullable(venteRepository.totalMontantTOByUserIdForFramContaining(user.getId(), "FRAM", mois))
                .orElse(0.0);

        double autresTo = Optional.ofNullable(venteRepository.totalMontantTOByUserIdForNonFram(user.getId(), "FRAM", mois))
                .orElse(0.0);

        double realise = Optional.ofNullable(venteRepository.totalMontant(user.getId(), mois))
                .orElse(0.0);

        double montantAssurance = Optional.ofNullable(venteRepository.totalMontantAssuranceByUserId(user.getId(), mois))
                .orElse(0.0);

        double nombreAssurance = Optional.ofNullable(venteRepository.countAssuranceSouscriteByUserId(user.getId(), mois))
                .orElse(0L);

        double nombreVente = Optional.ofNullable(venteRepository.countVentesByUserId(user.getId()))
                .orElse(0.0);


        bilan.setFramCroisieres(framCroisieres);
        bilan.setAutresTo(autresTo);
        bilan.setRealise(realise);
        bilan.setAssurances(nombreAssurance);
        bilan.setObjectif(user.getAgence().getObjectif());

        bilan.setPourcentageRealise(realise / bilan.getObjectif());
        bilan.setPourcentageFram(framCroisieres / bilan.getObjectif());
        bilan.setPourcentageAssurance(nombreVente != 0 ? nombreAssurance / nombreVente : 0);

        if (bilan.getPourcentageRealise() >= 1.0) {
            bilan.setTotalPrimesFram(framCroisieres / 1.2 * 0.01);
            bilan.setTotalPrimesAutre(autresTo / 1.2 * 0.005);
            bilan.setTotalPrimesAss(montantAssurance / 1.2 * 0.01);
        } else {
            bilan.setTotalPrimesFram(framCroisieres / 1.2 * 0.01 * 0.8);
            bilan.setTotalPrimesAutre(autresTo / 1.2 * 0.005 * 0.8);
            bilan.setTotalPrimesAss(montantAssurance / 1.2 * 0.01 * 0.8);
        }

        bilan.setTotalPrimesBrutes(bilan.getTotalPrimesAutre() + bilan.getTotalPrimesFram() + bilan.getTotalPrimesAss());

        bilan = bilanRepository.save(bilan);

        BilanDto.fromEntity(bilan);
    }



    @Override
    public BilanDto getBilanById(Long id) {
        // Recherche du Bilan par ID
        Bilan bilan = bilanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bilan non trouvé"));
        return BilanDto.fromEntity(bilan); // Retourner le DTO
    }


    @Override
    public void deleteBilan(Long id) {
        // Supprimer un Bilan par son ID
        if (!bilanRepository.existsById(id)) {
            throw new RuntimeException("Bilan non trouvé");
        }
        bilanRepository.deleteById(id); // Suppression du Bilan
    }

    @Override
    public BilanDto getBilanByUserId(Long id) {
        YearMonth moisPrecedent = YearMonth.now();
        Bilan bilan = bilanRepository.findByUserIdAndMoisBilan(id, moisPrecedent.minusMonths(1))
                .orElseThrow(() -> new RuntimeException("Bilan non trouvé"));
        return BilanDto.fromEntity(bilan); // Retourner le DTO
    }

    @Override
    public Map<String, Object> getBilanByAgence(Long agenceId) {
        // Récupérer l'agence pour obtenir son objectif
        AgenceVoyage agence = agenceVoyageRepository.findById(agenceId)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée"));

        // Récupérer les bilans des utilisateurs de cette agence
        List<Bilan> bilans = bilanRepository.findByAgenceId(agenceId);

        // Calcul du total réalisé par l'agence
        double totalRealise = bilans.stream().mapToDouble(Bilan::getRealise).sum();

        // Calcul du pourcentage réalisé par rapport à l'objectif de l'agence
        double pourcentageRealise = (agence.getObjectif() > 0) ? (totalRealise / agence.getObjectif()) * 100 : 0;

        // Construire la réponse sous forme de Map
        Map<String, Object> bilanAgence = new HashMap<>();
        bilanAgence.put("agenceId", agenceId);
        bilanAgence.put("nomAgence", agence.getNom());
        bilanAgence.put("objectif", agence.getObjectif());
        bilanAgence.put("totalRealise", totalRealise);
        bilanAgence.put("pourcentageRealise", pourcentageRealise);
        bilanAgence.put("bilans", bilans); // Liste des bilans individuels

        return bilanAgence;
    }

    @Override
    public BilanDto getBilanByMoisAndUserId(Long userId, YearMonth choisirMois){
        Optional<Bilan> bilan = bilanRepository.findByUserIdAndMoisBilan(userId, choisirMois);
        return bilan.map(BilanDto::fromEntity).orElse(null);
    }
}
