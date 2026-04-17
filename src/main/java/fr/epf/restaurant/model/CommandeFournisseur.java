package fr.epf.restaurant.model;

import java.sql.Date;
import java.util.List;

import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;

public class CommandeFournisseur {
    private long id;
    private Fournisseur fournisseur;
    private Date dateCommande;
    private String statut;
    private List<CreerCommandeFournisseurRequest.LigneCommandeFournisseur> lignes;

    public CommandeFournisseur(long id, Fournisseur fournisseur, Date dateCommande, String statut,
            List<CreerCommandeFournisseurRequest.LigneCommandeFournisseur> lignes) {
        this.id = id;
        this.fournisseur = fournisseur;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.lignes = lignes;
    }

    public CommandeFournisseur() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public List<CreerCommandeFournisseurRequest.LigneCommandeFournisseur> getLignes() {
        return lignes;
    }

    public void setLignes(List<CreerCommandeFournisseurRequest.LigneCommandeFournisseur> lignes) {
        this.lignes = lignes;
    }

}
