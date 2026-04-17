package fr.epf.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.epf.restaurant.dao.ClientDao;
import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.CreerCommandeClientRequest.LigneCommandeClient;
import fr.epf.restaurant.exception.ResourceNotFoundException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import fr.epf.restaurant.model.Client;
import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.PlatIngredient;

@Service
public class CommandeClientService {
    private final CommandeClientDao commandeClientDao;
    private final IngredientDao ingredientDao;
    private final ClientDao clientDao;

    public CommandeClientService(CommandeClientDao commandeClientDao, IngredientDao ingredientDao,
            ClientDao clientDao) {
        this.commandeClientDao = commandeClientDao;
        this.ingredientDao = ingredientDao;
        this.clientDao = clientDao;
    }

    public List<CommandeClient> findAll(String statut) {
        if (statut != null) {
            return commandeClientDao.findByStatut(statut);
        }
        return commandeClientDao.findAll();
    }

    public CommandeClient findDetailCommandeClient(Long id) {
        CommandeClient commande = commandeClientDao.findById(id);
        if (commande == null) {
            throw new ResourceNotFoundException("La commande client avec l'ID " + id + " n'existe pas.");
        }
        return commande;
    }

    public CommandeClient creerCommande(CreerCommandeClientRequest creerCommandeClientRequest) {
        Client client = clientDao.findById(creerCommandeClientRequest.getClientId());

        if (client == null) {
            throw new ResourceNotFoundException(
                    "Le client avec l'ID " + creerCommandeClientRequest.getClientId() + " n'existe pas.");
        }

        Long idCommande = commandeClientDao.createCommandeClient(creerCommandeClientRequest);

        return commandeClientDao.findById(idCommande);
    }

    public void delete(long id) {
        CommandeClient commande = commandeClientDao.findById(id);
        if (commande == null) {
            throw new ResourceNotFoundException("La commande client avec l'ID " + id + " n'existe pas.");
        }
        commandeClientDao.delete(id);
    }

    @Transactional
    public void preparer(long commandeId) {
        CommandeClient commandeClient = commandeClientDao.findById(commandeId);

        if (commandeClient == null) {
            throw new ResourceNotFoundException(
                    "La commande client avec l'ID " + commandeId + " n'existe pas.");
        }

        if (!"EN_ATTENTE".equals(commandeClient.getStatut())) {
            throw new StatutInvalideException(
                    "Une commande ne peut passer en préparation que si son statut est EN_ATTENTE.");
        }

        List<LigneCommandeClient> ligneCommandeClients = commandeClient.getLignes();

        for (LigneCommandeClient ligne : ligneCommandeClients) {
            int quantitePlat = ligne.getQuantite();
            List<PlatIngredient> ingredientsPlat = ligne.getPlat().getIngredients();

            for (PlatIngredient platIngredient : ingredientsPlat) {
                double besoinTotal = platIngredient.getQuantiteRequise() * quantitePlat;
                double stockActuel = platIngredient.getIngredient().getStockActuel();

                if (stockActuel < besoinTotal) {
                    throw new StockInsuffisantException(
                            "Stock insuffisant pour : " + platIngredient.getIngredient().getNom());
                }
            }
        }

        for (LigneCommandeClient ligne : ligneCommandeClients) {
            int quantitePlat = ligne.getQuantite();
            List<PlatIngredient> ingredientsPlat = ligne.getPlat().getIngredients();

            for (PlatIngredient platIngredient : ingredientsPlat) {
                double besoinTotal = platIngredient.getQuantiteRequise() * quantitePlat;
                ingredientDao.updateStock(platIngredient.getIngredient().getId(), -besoinTotal);
            }
        }

        commandeClientDao.preparer(commandeId);
    }

    public void servir(long commandeId) {
        CommandeClient commandeClient = commandeClientDao.findById(commandeId);

        if (commandeClient == null) {
            throw new ResourceNotFoundException(
                    "La commande client avec l'ID " + commandeId + " n'existe pas.");
        }

        if (!"EN_PREPARATION".equals(commandeClient.getStatut())) {
            throw new StatutInvalideException(
                    "Une commande ne peut être servie que si son statut est en preparation.");
        }
        commandeClientDao.servir(commandeId);
    }

}
