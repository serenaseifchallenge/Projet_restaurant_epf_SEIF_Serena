package fr.epf.restaurant.model;

import java.sql.Date;
import java.util.List;


import fr.epf.restaurant.dto.CreerCommandeClientRequest;

public class CommandeClient {
    private long id;
    private Client client;
    private Date dateCommande;
    private String statut;
    private List<CreerCommandeClientRequest.LigneCommandeClient> lignes;

    //constructeurs
    public CommandeClient() {
    }

    public CommandeClient(long id, Client client, Date dateCommande, String statut,
            List<CreerCommandeClientRequest.LigneCommandeClient> lignes) {
        this.id = id;
        this.client = client;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.lignes = lignes;
    }

    //getteurs et setteurs
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<CreerCommandeClientRequest.LigneCommandeClient> getLignes() {
        return lignes;
    }

    public void setLignes(List<CreerCommandeClientRequest.LigneCommandeClient> lignes) {
        this.lignes = lignes;
    }

}
