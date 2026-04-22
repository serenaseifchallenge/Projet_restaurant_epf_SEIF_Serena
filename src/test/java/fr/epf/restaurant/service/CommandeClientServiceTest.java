package fr.epf.restaurant.service;

import fr.epf.restaurant.TestConfig;
import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.exception.ResourceNotFoundException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.Ingredient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class CommandeClientServiceTest {

    @Autowired
    private CommandeClientService commandeClientService;

    @Autowired
    private IngredientDao ingredientDao;

    @Autowired
    private CommandeClientDao commandeClientDao;

    @Autowired
    private StockService stockService;

    @Autowired
    private CommandeFournisseurService commandeFournisseurService;

    @Test
    void testCreerCommandeClient() {
        // creer une commande avec un client inexistant, résultat attendue : erreur ressource not found
        CreerCommandeClientRequest request = new CreerCommandeClientRequest();
        request.setClientId(-1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            commandeClientService.creerCommande(request);
        });
    }

    @Test
    void testStockCommande() {
        //creer une enorme commande,
        //résultat attendue : erreur stock insuffisant
        CreerCommandeClientRequest.LigneCommandeClient ligneEnorme = new CreerCommandeClientRequest.LigneCommandeClient(
                1L, 1000);

        CreerCommandeClientRequest grosseCommande = new CreerCommandeClientRequest();
        grosseCommande.setClientId(1L);
        grosseCommande.setLignes(List.of(ligneEnorme));

        CommandeClient commandeClient = commandeClientService.creerCommande(grosseCommande);

        assertThrows(StockInsuffisantException.class, () -> {
            commandeClientService.preparer(commandeClient.getId());
        });
    }

    @Test
    void testIngredientsStockStatus() {
        // changer le stock d'oeuf à stockActuel < seuilAlerte,
        // résultat attendu : oeuf en alerte et lait sans alerte (quantité suffisante)
        Ingredient oeufs = ingredientDao.findById(2L);
        oeufs.setStockActuel(1); // forcer l'ajout d'oeufs dans la liste d'alerte
        ingredientDao.update(oeufs);

        List<AlerteStockDto> alertes = stockService.findIngredientsSousAlerte();

        // verifie que les oeufs font partie de la liste
        boolean oeufsEnAlerte = alertes.stream()
                .anyMatch(a -> a.getNomIngredient().equalsIgnoreCase("Oeufs"));

        // Verify lait ne fait pas partie de la liste
        boolean laitEnAlerte = alertes.stream()
                .anyMatch(a -> a.getNomIngredient().equalsIgnoreCase("Lait"));

        assertTrue(oeufsEnAlerte);
        assertFalse(laitEnAlerte);
    }

    @Test
    void testMiseAJourStock() {
        // creer une commandeFournisseur,
        // résultat attendu : stock de l'ingredient qu'on commande augmente
        double ingredientAvantCommande = ingredientDao.findById(1L).getStockActuel();
        CreerCommandeFournisseurRequest.LigneCommandeFournisseur ligneCommandeFournisseur =
        new CreerCommandeFournisseurRequest.LigneCommandeFournisseur(
                1L, 2, 30);

        CreerCommandeFournisseurRequest creerCommandeFournisseur = new CreerCommandeFournisseurRequest();
        creerCommandeFournisseur.setFournisseurId(1L);
        creerCommandeFournisseur.setLignes(List.of(ligneCommandeFournisseur));

        CommandeFournisseur commandeFournisseur = commandeFournisseurService.creerCommande(creerCommandeFournisseur);

        commandeFournisseurService.envoyer(commandeFournisseur.getId());
        commandeFournisseurService.recevoir(commandeFournisseur.getId());
        double ingredientApresCommande = ingredientDao.findById(1L).getStockActuel();

        double stockAttendu = ingredientAvantCommande + 2;
        assertEquals(stockAttendu, ingredientApresCommande, 0.001);

    }

    @Test
    void testRecommandationCommande() {
        // Tester la Recommandation d'une commande
        // résultat attendu : recommandation fournisseur moins cher + quantité = 2*(seuil-1)
        Ingredient oeufs = ingredientDao.findById(2L);
        double seuil = oeufs.getSeuilAlerte();

        oeufs.setStockActuel(1);
        ingredientDao.update(oeufs);

        RecommandationDto recu = stockService.getMoinsCher(oeufs.getId());
        double quantiteAttendue = 2 * (seuil - oeufs.getStockActuel());

        assertEquals(2L, recu.getFournisseurId());
        assertEquals(quantiteAttendue, recu.getQuantiteRecommandee(), 0.001);
    }

    @Test
    void testPreparerCommandeChangeStatut() {
        // CommandeClient crée --> statut en préparation, commande servie --> statut servie
        CreerCommandeClientRequest.LigneCommandeClient ligne =
                new CreerCommandeClientRequest.LigneCommandeClient(1L, 2);

        CreerCommandeClientRequest request = new CreerCommandeClientRequest();
        request.setClientId(1L);
        request.setLignes(List.of(ligne));

        CommandeClient commande = commandeClientService.creerCommande(request);

        commandeClientService.preparer(commande.getId());

        CommandeClient maj = commandeClientDao.findById(commande.getId());
        assertEquals("EN_PREPARATION", maj.getStatut());

        commandeClientService.servir((commande.getId()));
        CommandeClient majS = commandeClientDao.findById(commande.getId());
        assertEquals("SERVIE", majS.getStatut());
    }

    @Test
    void testIngredientExactementAuSeuil() {
        //tester un ingredient : stock_actuel=seuil_alerte
        // résultat attendu: alerte ne doit pas être présente dans la liste des alertes
        Ingredient ingredient = ingredientDao.findById(2L);

        ingredient.setStockActuel(ingredient.getSeuilAlerte());
        ingredientDao.update(ingredient);

        List<AlerteStockDto> alertes = stockService.findIngredientsSousAlerte();

        boolean alertesList = alertes.stream()
                .anyMatch(a -> a.getNomIngredient().equalsIgnoreCase(ingredient.getNom()));

        assertFalse(alertesList);
    }

    @Test
    void testServirCommandeNonPreparee() {
        // servir une commande non préparer --> erreur statut invalide
        CreerCommandeClientRequest.LigneCommandeClient ligne =
                new CreerCommandeClientRequest.LigneCommandeClient(1L, 1);

        CreerCommandeClientRequest request = new CreerCommandeClientRequest();
        request.setClientId(1L);
        request.setLignes(List.of(ligne));

        CommandeClient commande = commandeClientService.creerCommande(request);

        assertThrows(RuntimeException.class, () -> {
            commandeClientService.servir(commande.getId());
        });
    }

    @Test
    void testPreparerCommandeDejaServie() {
        //repreparer une commande déjà servie --> erreur statut invalide
        CreerCommandeClientRequest.LigneCommandeClient ligne =
                new CreerCommandeClientRequest.LigneCommandeClient(1L, 1);

        CreerCommandeClientRequest request = new CreerCommandeClientRequest();
        request.setClientId(1L);
        request.setLignes(List.of(ligne));

        CommandeClient commande = commandeClientService.creerCommande(request);
        commandeClientService.preparer(commande.getId());
        commandeClientService.servir(commande.getId());

        assertThrows(StatutInvalideException.class, () -> {
            commandeClientService.preparer(commande.getId());
        });
    }
}