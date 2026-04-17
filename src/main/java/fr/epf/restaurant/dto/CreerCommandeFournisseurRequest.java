package fr.epf.restaurant.dto;

import java.util.List;
import fr.epf.restaurant.model.Ingredient;

public class CreerCommandeFournisseurRequest {
    private long fournisseurId;
    private List<LigneCommandeFournisseur> lignes;

    public CreerCommandeFournisseurRequest() {
    }

    public CreerCommandeFournisseurRequest(long fournisseurId, List<LigneCommandeFournisseur> lignes) {
        this.fournisseurId = fournisseurId;
        this.lignes = lignes;
    }

    public long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public List<LigneCommandeFournisseur> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeFournisseur> lignes) {
        this.lignes = lignes;
    }

    public static class LigneCommandeFournisseur {
        private long ingredientId;
        private double quantite;
        private float prixUnitaire;
        private Ingredient ingredient;

        public LigneCommandeFournisseur(long ingredientId, double quantite, float prixUnitaire) {
            this.ingredientId = ingredientId;
            this.quantite = quantite;
            this.prixUnitaire = prixUnitaire;
        }

        public LigneCommandeFournisseur() {
        }

        public double getQuantiteCommandee() {
            return quantite;
        }

        public void setQuantite(double quantite) {
            this.quantite = quantite;
        }

        public double getQuantite() {
            return quantite;
        }

        public float getPrixUnitaire() {
            return prixUnitaire;
        }

        public void setPrixUnitaire(float prixUnitaire) {
            this.prixUnitaire = prixUnitaire;
        }

        public long getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        public void setIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
        }
    }
}