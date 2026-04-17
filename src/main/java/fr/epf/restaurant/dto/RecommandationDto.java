package fr.epf.restaurant.dto;

import java.math.BigDecimal;

public class RecommandationDto {
    private Long fournisseurId;
    private String fournisseurNom;
    private BigDecimal prixUnitaire;
    private double quantiteRecommandee;

    public RecommandationDto() {
    }

    public RecommandationDto(Long fournisseurId, String fournisseurNom, BigDecimal prixUnitaire,
            double quantiteRecommandee) {
        this.fournisseurId = fournisseurId;
        this.fournisseurNom = fournisseurNom;
        this.prixUnitaire = prixUnitaire;
        this.quantiteRecommandee = quantiteRecommandee;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getFournisseurNom() {
        return fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getQuantiteRecommandee() {
        return quantiteRecommandee;
    }

    public void setQuantiteRecommandee(double quantiteRecommandee) {
        this.quantiteRecommandee = quantiteRecommandee;
    }

}
