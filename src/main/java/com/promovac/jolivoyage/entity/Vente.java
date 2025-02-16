package com.promovac.jolivoyage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
public class Vente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant total de la vente doit être positif.")
    private BigDecimal venteTotal; // Montant total de la vente

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères.")
    private String nom; // Nom de l'utilisateur

    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.")
    private String prenom; // Prénom de l'utilisateur

    @NotBlank(message = "Le numéro de dossier est obligatoire.")
    @Size(max = 20, message = "Le numéro de dossier ne doit pas dépasser 20 caractères.")
    private String numeroDossier; // Numéro de dossier

    @Min(value = 1, message = "Le nombre de personnes (pax) doit être au moins égal à 1.")
    private int pax; // Nombre de personnes

    private LocalDate dateValidation; // Date de validation

    @FutureOrPresent(message = "La date de départ doit être aujourd'hui ou dans le futur.")
    private LocalDate dateDepart; // Date de départ

    @NotBlank(message = "Le nom du tour opérateur est obligatoire.")
    @Size(max = 100, message = "Le nom du tour opérateur ne doit pas dépasser 100 caractères.")
    private String tourOperateur; // Tour opérateur (TO)

    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant de l'assurance doit être positif.")
    private double montantAssurance; // Montant de l'assurance

    @DecimalMin(value = "0.0", inclusive = true, message = "Les frais d'agence doivent être positifs.")
    private double fraisAgence; // Frais d'agence

    @DecimalMin(value = "0.0", inclusive = true, message = "Le total sans assurance doit être positif.")
    private double totalSansAssurance;

    private boolean assurance; // Indique si une assurance a été souscrite

    @Column(updatable = false)
    private YearMonth transactionDate; // Mois de la transaction

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user; // Association avec l'utilisateur


}
