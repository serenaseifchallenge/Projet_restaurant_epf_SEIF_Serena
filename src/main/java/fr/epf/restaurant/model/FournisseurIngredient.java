package fr.epf.restaurant.model;

public class FournisseurIngredient {
    private long fournisseurId;
    private long ingredientId;
    private float prixUnitaire;

    public FournisseurIngredient(long fournisseurId, long ingredientId, float prixUnitaire) {
        this.fournisseurId = fournisseurId;
        this.ingredientId = ingredientId;
        this.prixUnitaire = prixUnitaire;
    }

    public FournisseurIngredient() {
    }

    public long getFournisseurId() {
        return fournisseurId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }

}
