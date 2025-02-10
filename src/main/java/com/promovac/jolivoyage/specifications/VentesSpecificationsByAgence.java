package com.promovac.jolivoyage.specifications;

import com.promovac.jolivoyage.entity.Vente;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VentesSpecificationsByAgence {

    /**
     * Filtre les ventes par l'ID de l'agence.
     *
     * @param agenceId L'ID de l'agence pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes liées à l'agence.
     */
    public static Specification<Vente> hasAgenceId(Long agenceId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("agence").get("id"), agenceId);
    }

    /**
     * Filtre les ventes par l'ID de l'utilisateur.
     *
     * @param userId L'ID de l'utilisateur pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes liées à l'utilisateur.
     */
    public static Specification<Vente> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    /**
     * Filtre les ventes par le nom de l'utilisateur.
     *
     * @param nom Le nom de l'utilisateur pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes liées à un utilisateur ayant un nom spécifique.
     */
    public static Specification<Vente> hasNomUser(String nom) {
        return (root, query, criteriaBuilder) ->
                (nom == null || nom.isEmpty()) ? null : criteriaBuilder.like(root.get("user").get("nom"), "%" + nom + "%");
    }

    /**
     * Filtre les ventes par le prénom de l'utilisateur.
     *
     * @param prenom Le prénom de l'utilisateur pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes liées à un utilisateur ayant un prénom spécifique.
     */
    public static Specification<Vente> hasPrenomUser(String prenom) {
        return (root, query, criteriaBuilder) ->
                (prenom == null || prenom.isEmpty()) ? null : criteriaBuilder.like(root.get("user").get("prenom"), "%" + prenom + "%");
    }

    /**
     * Filtre les ventes par le nom de la vente.
     *
     * @param nom Le nom de la vente pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes ayant un nom spécifique.
     */
    public static Specification<Vente> hasNom(String nom) {
        return (root, query, criteriaBuilder) ->
                nom == null ? null : criteriaBuilder.like(root.get("nom"), "%" + nom + "%");
    }

    /**
     * Filtre les ventes par le prénom de la vente.
     *
     * @param prenom Le prénom de la vente pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes ayant un prénom spécifique.
     */
    public static Specification<Vente> hasPrenom(String prenom) {
        return (root, query, criteriaBuilder) ->
                prenom == null ? null : criteriaBuilder.like(root.get("prenom"), "%" + prenom + "%");
    }

    /**
     * Filtre les ventes par le numéro de dossier.
     *
     * @param numeroDossier Le numéro de dossier pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes ayant un numéro de dossier spécifique.
     */
    public static Specification<Vente> hasNumeroDossier(String numeroDossier) {
        return (root, query, criteriaBuilder) ->
                numeroDossier == null ? null : criteriaBuilder.like(root.get("numeroDossier"), "%" + numeroDossier + "%");
    }

    /**
     * Filtre les ventes où la date de départ est après une certaine date.
     *
     * @param dateDepart La date de départ pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes ayant une date de départ après la date donnée.
     */
    public static Specification<Vente> hasDateDepartAfter(LocalDate dateDepart) {
        return (root, query, criteriaBuilder) ->
                dateDepart == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dateDepart"), dateDepart);
    }

    /**
     * Filtre les ventes par la date de validation.
     *
     * @param dateValidation La date de validation pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes ayant une date de validation spécifique.
     */
    public static Specification<Vente> hasDateValidation(LocalDate dateValidation) {
        return (root, query, criteriaBuilder) ->
                dateValidation == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dateValidation"), dateValidation);
    }

    /**
     * Filtre les ventes par le statut d'assurance.
     *
     * @param assurance Le statut d'assurance (true/false) pour filtrer les ventes.
     * @return Une spécification pour rechercher les ventes avec un statut d'assurance spécifique.
     */
    public static Specification<Vente> hasAssurance(Boolean assurance) {
        return (root, query, criteriaBuilder) ->
                assurance == null ? null : criteriaBuilder.equal(root.get("assurance"), assurance);
    }
}
