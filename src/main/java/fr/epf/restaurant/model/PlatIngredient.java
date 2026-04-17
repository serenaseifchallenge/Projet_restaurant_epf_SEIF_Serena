package fr.epf.restaurant.model;

public class PlatIngredient {
    private long platId;
    private Ingredient ingredient;
    private double quantiteRequise;

    public PlatIngredient() {
    }

    public PlatIngredient(long platId, Ingredient ingredient, double quantiteRequise) {
        this.platId = platId;
        this.ingredient = ingredient;
        this.quantiteRequise = quantiteRequise;
    }

    public long getPlatId() {
        return platId;
    }

    public void setPlatId(long platId) {
        this.platId = platId;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantiteRequise(double quantiteRequise) {
        this.quantiteRequise = quantiteRequise;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantiteRequise() {
        return quantiteRequise;
    }

}
