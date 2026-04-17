package fr.epf.restaurant.dto;

import java.util.List;

import fr.epf.restaurant.model.Plat;

public class CreerCommandeClientRequest {
    private Long clientId;
    private List<LigneCommandeClient> lignes;

    // Constructeurs
    public CreerCommandeClientRequest(Long clientId, List<LigneCommandeClient> lignes) {
        this.clientId = clientId;
        this.lignes = lignes;
    }

    public CreerCommandeClientRequest() {
    }

    // Getters et Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<LigneCommandeClient> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeClient> lignes) {
        this.lignes = lignes;
    }

    // Sous-classe pour les lignes de commande
    public static class LigneCommandeClient {
        private Long platId;
        private Integer quantite;
        private Plat plat;

        public LigneCommandeClient(Long platId, Integer quantite) {
            this.platId = platId;
            this.quantite = quantite;
        }

        public LigneCommandeClient() {
        }

        public Long getPlatId() {
            return platId;
        }

        public Integer getQuantite() {
            return quantite;
        }

        public void setQuantite(Integer quantite) {
            this.quantite = quantite;
        }

        public void setPlatId(Long platId) {
            this.platId = platId;
        }

        public Plat getPlat() {
            return plat;
        }

        public void setPlat(Plat plat) {
            this.plat = plat;
        }
    }
}