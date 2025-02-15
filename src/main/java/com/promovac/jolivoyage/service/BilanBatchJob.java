package com.promovac.jolivoyage.service;

import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.service.interf.BilanService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BilanBatchJob {

    private final UserRepository userRepository;
    private final BilanService bilanService;


    public BilanBatchJob(UserRepository userRepository, BilanService bilanService) {
        this.userRepository = userRepository;
        this.bilanService = bilanService;
    }

    /**
     * Exécute la génération des bilans pour tous les utilisateurs chaque 1er du mois à minuit.
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void generateMonthlyBilans() {
        List<User> allUsers = userRepository.findAll(); // Récupère tous les utilisateurs
        YearMonth moisActuel = YearMonth.now();

        for (User user : allUsers) {
            bilanService.saveOrUpdateBilan(user.getId(), moisActuel);
        }

        System.out.println("🚀 Bilans mensuels générés pour tous les utilisateurs !");
    }
}
