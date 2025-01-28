package com.promovac.jolivoyage.service;


import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.entity.Bilan;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.BilanRepository;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.repository.VenteRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class BilanServiceImpl implements BilanService {

    @Autowired
    private BilanRepository bilanRepository;

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private UserRepository myAppUserRepository;

    @Override
    public BilanDto saveOrUpdateBilan(Long userId) {
        // Récupérer l'utilisateur
        User user = myAppUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec l'ID : " + userId));

        // Rechercher le bilan de l'utilisateur, ou en créer un nouveau si non trouvé
        Bilan bilan = bilanRepository.findByUserId(userId).orElseGet(Bilan::new);

        // Associer l'utilisateur au bilan
        bilan.setUser(user);

        // Calculs des montants avec gestion des valeurs nulles
        double framCroisieres = venteRepository.totalMontantTOByUserIdForFramContaining(user.getId(), "%" + "FRAM" + "%") != null
                ? venteRepository.totalMontantTOByUserIdForFramContaining(user.getId(), "%" + "FRAM" + "%") : 0.0;
        double autresTo = venteRepository.totalMontantTOByUserIdForNonFram(user.getId(), "%" + "FRAM" + "%") != null
                ? venteRepository.totalMontantTOByUserIdForNonFram(user.getId(), "%" + "FRAM" + "%") : 0.0;
        double realise = venteRepository.totalMontant(user.getId()) != null
                ? venteRepository.totalMontant(user.getId()) : 0.0;
        double objectif = 100000;
        double assurance = venteRepository.totalMontantAssuranceByUserId(user.getId()) != null
                ? venteRepository.totalMontantAssuranceByUserId(user.getId()) : 0.0;

        // Mise à jour des valeurs du bilan
        bilan.setFramCroisieres(framCroisieres);
        bilan.setAutresTo(autresTo);
        bilan.setRealise(realise);
        bilan.setObjectif(objectif);
        bilan.setAssurances(assurance);

        // Calcul des pourcentages
        bilan.setPourcentageRealise(realise / objectif);
        bilan.setPourcentageFram(framCroisieres / objectif);
        bilan.setPourcentageAssurance(assurance / objectif);

        // Calcul des primes
        if (bilan.getPourcentageRealise() >= 1.0) {
            bilan.setTotalPrimesFram(framCroisieres / 1.2 * 0.01);
            bilan.setTotalPrimesAutre(autresTo / 1.2 * 0.005);
            bilan.setTotalPrimesAss(assurance / 1.2 * 0.01);
        } else {
            bilan.setTotalPrimesFram(framCroisieres / 1.2 * 0.01 * 0.8);
            bilan.setTotalPrimesAutre(autresTo / 1.2 * 0.005 * 0.8);
            bilan.setTotalPrimesAss(assurance / 1.2 * 0.01 * 0.8);
        }

        // Calcul du total des primes brutes
        bilan.setTotalPrimesBrutes(bilan.getTotalPrimesAutre() + bilan.getTotalPrimesFram() + bilan.getTotalPrimesAss());

        // Sauvegarder ou mettre à jour le bilan
        bilan = bilanRepository.save(bilan);

        // Retourner le DTO
        return BilanDto.fromEntity(bilan);
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
}
