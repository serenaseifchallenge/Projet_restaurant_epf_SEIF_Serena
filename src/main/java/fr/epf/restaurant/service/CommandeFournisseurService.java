package fr.epf.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.epf.restaurant.dao.CommandeFournisseurDao;
import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest.LigneCommandeFournisseur;
import fr.epf.restaurant.exception.ResourceNotFoundException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.Ingredient;

@Service
public class CommandeFournisseurService {
    private final CommandeFournisseurDao commandeFournisseurDao;
    private final IngredientDao ingredientDao;
    private final FournisseurDao fournisseurDao;

    public CommandeFournisseurService(CommandeFournisseurDao commandeFournisseurDao, IngredientDao ingredientDao,
            FournisseurDao fournisseurDao) {
        this.commandeFournisseurDao = commandeFournisseurDao;
        this.ingredientDao = ingredientDao;
        this.fournisseurDao = fournisseurDao;
    }

    public List<CommandeFournisseur> findAll(String statut) {
        if (statut != null) {
            return commandeFournisseurDao.findByStatut(statut);
        }
        return commandeFournisseurDao.findAll();
    }

    public CommandeFournisseur findDetailCommandeFournisseur(Long id) {
        CommandeFournisseur commande = commandeFournisseurDao.findById(id);
        if (commande == null) {
            throw new ResourceNotFoundException(
                    "La commande fournisseur avec l'ID " + id + " n'existe pas.");
        }
        return commande;
    }

    public CommandeFournisseur creerCommande(CreerCommandeFournisseurRequest request) {
        if (fournisseurDao.findById(request.getFournisseurId()) == null) {
            throw new ResourceNotFoundException(
                    "Le fournisseur avec l'ID " + request.getFournisseurId() + " n'existe pas.");
        }

        Long id = commandeFournisseurDao.createcommandeFournisseur(request);
        return commandeFournisseurDao.findById(id);
    }

    public void delete(long id) {
        commandeFournisseurDao.delete(id);
    }

    public void envoyer(long commandeId) {
        CommandeFournisseur commande = commandeFournisseurDao.findById(commandeId);

        if (commande == null) {
            throw new ResourceNotFoundException(
                    "La commande fournisseur avec l'ID " + commandeId + " n'existe pas.");
        }

        if (!"EN_ATTENTE".equals(commande.getStatut())) {
            throw new StatutInvalideException(
                    "Une commande fournisseur ne peut être envoyée que si son statut est en attente.");
        }

        commandeFournisseurDao.envoyer(commandeId);
    }

    @Transactional
    public void recevoir(long commandeId) {
        CommandeFournisseur commandeFournisseur = commandeFournisseurDao.findById(commandeId);

        if (commandeFournisseur == null) {
            throw new ResourceNotFoundException(
                    "La commande fournisseur avec l'ID " + commandeId + " n'existe pas.");
        }
        if (!"ENVOYEE".equals(commandeFournisseur.getStatut())) {
            throw new StatutInvalideException(
                    "Une commande fournisseur ne peut être reçue que si son statut est envoyee.");
        }

        List<LigneCommandeFournisseur> lignes = commandeFournisseur.getLignes();
        for (LigneCommandeFournisseur ligne : lignes) {
            double quantiteIngredients = ligne.getQuantiteCommandee();
            Ingredient ingredient = ligne.getIngredient();
            ingredientDao.updateStock(ingredient.getId(), quantiteIngredients);
        }

        commandeFournisseurDao.recevoir(commandeId);
    }

}
