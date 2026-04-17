package fr.epf.restaurant.model;

public class LigneCommandeClient {
    private long platId;
    private long ingredientId;
    private double quantiteRequise;

    public LigneCommandeClient(long platId, long ingredientId, double quantiteRequise) {
        this.platId = platId;
        this.ingredientId = ingredientId;
        this.quantiteRequise = quantiteRequise;
    }

    public LigneCommandeClient() {
    }

    public long getPlatId() {
        return platId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public double getQuantiteRequise() {
        return quantiteRequise;
    }

    public void setPlatId(long platId) {
        this.platId = platId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setQuantiteRequise(double quantiteRequise) {
        this.quantiteRequise = quantiteRequise;
    }

}
