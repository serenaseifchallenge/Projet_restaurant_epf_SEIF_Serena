package fr.epf.restaurant.model;

import java.util.List;

public class Plat {
    private long id;
    private String nom;
    private String description;
    private float prix;
    private List<PlatIngredient> ingredients;

    public Plat(long id, String nom, String description, float prix, List<PlatIngredient> ingredients) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.ingredients = ingredients;
    }

    public Plat() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public float getPrix() {
        return prix;
    }

    public List<PlatIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<PlatIngredient> ingredients) {
        this.ingredients = ingredients;
    }

}
