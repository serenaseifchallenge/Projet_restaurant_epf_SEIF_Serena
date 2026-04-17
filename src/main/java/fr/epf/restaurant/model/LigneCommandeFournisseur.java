package fr.epf.restaurant.model;

public class LigneCommandeFournisseur {
    private long id;
    private long commandeFournisseurId;
    private long ingredientId;
    private double quantiteCommandee;
    private float prixUnitaire;
    public LigneCommandeFournisseur() {
    }
    public LigneCommandeFournisseur(long id, long commandeFournisseurId, long ingredientId, double quantiteCommandee,
            float prixUnitaire) {
        this.id = id;
        this.commandeFournisseurId = commandeFournisseurId;
        this.ingredientId = ingredientId;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
    }
    public long getId() {
        return id;
    }
    public long getCommandeFournisseurId() {
        return commandeFournisseurId;
    }
    public long getIngredientId() {
        return ingredientId;
    }
    public double getQuantiteCommandee() {
        return quantiteCommandee;
    }
    public float getPrixUnitaire() {
        return prixUnitaire;
    }
}
