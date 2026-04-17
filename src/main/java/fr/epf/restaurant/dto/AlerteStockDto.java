package fr.epf.restaurant.dto;

public class AlerteStockDto {
    private Long ingredientId;
    private String nomIngredient;
    private Double quantiteACommander;

    public AlerteStockDto() {
    }

    public AlerteStockDto(Long ingredientId, String nomIngredient, Double quantiteACommander) {
        this.ingredientId = ingredientId;
        this.nomIngredient = nomIngredient;
        this.quantiteACommander = quantiteACommander;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public String getNomIngredient() {
        return nomIngredient;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setNomIngredient(String nomIngredient) {
        this.nomIngredient = nomIngredient;
    }

    public void setQuantiteACommander(Double quantiteACommander) {
        this.quantiteACommander = quantiteACommander;
    }

    public Double getQuantiteACommander() {
        return quantiteACommander;
    }

}
