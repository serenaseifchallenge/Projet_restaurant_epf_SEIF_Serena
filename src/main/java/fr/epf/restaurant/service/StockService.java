package fr.epf.restaurant.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.model.Ingredient;

@Service
public class StockService {

    private IngredientDao ingredientDao;

    public StockService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    public List<AlerteStockDto> findIngredientsSousAlerte() {
        List<Ingredient> ingredients = ingredientDao.findSousAlerte();

        return ingredients.stream().map(ingredient -> {
            AlerteStockDto dto = new AlerteStockDto();
            dto.setIngredientId(ingredient.getId());
            dto.setNomIngredient(ingredient.getNom());
            dto.setQuantiteACommander(2 * (ingredient.getSeuilAlerte() - ingredient.getStockActuel()));
            return dto;
        }).toList();
    }

    public List<Map<String, Object>> getPrix(long ingredientId) {
        return ingredientDao.findPrixParIngredient(ingredientId);
    }

    /*
     * > **Recommandation de réapprovisionnement (`GET
     * /api/ingredients/{id}/recommandation`)**
     * > - Retourne le fournisseur le **moins cher** pour cet ingrédient (prix
     * unitaire le plus bas dans `FOURNISSEUR_INGREDIENT`)
     * > - La **quantité recommandée** est calculée ainsi :
     * > - Si `seuilAlerte > stockActuel` : quantité = `2 × (seuilAlerte −
     * stockActuel)`
     * > - Sinon (stock exactement au seuil) : quantité = `seuilAlerte`
     */
    public RecommandationDto getMoinsCher(long ingredientId) {
        List<Map<String, Object>> prix = ingredientDao.findPrixParIngredient(ingredientId);

        if (prix.isEmpty()) {
            return null;
        }

        Map<String, Object> moinsCher = prix.get(0);

        for (Map<String, Object> ligne : prix) {
            BigDecimal prixActuel = (BigDecimal) ligne.get("prixUnitaire");
            BigDecimal prixMin = (BigDecimal) moinsCher.get("prixUnitaire");

            if (prixActuel.compareTo(prixMin) < 0) {
                moinsCher = ligne;
            }

        }

        Ingredient ingredient = ingredientDao.findById(ingredientId);

        double quantiteRecommandee;

        if (ingredient.getSeuilAlerte() > ingredient.getStockActuel()) {
            quantiteRecommandee = 2 * (ingredient.getSeuilAlerte() - ingredient.getStockActuel());
        }
        else {
            quantiteRecommandee = ingredient.getSeuilAlerte();
        }

        RecommandationDto dto = new RecommandationDto();
        dto.setFournisseurId(((Number) moinsCher.get("fournisseurId")).longValue());
        dto.setFournisseurNom((String) moinsCher.get("fournisseurNom"));
        dto.setPrixUnitaire((BigDecimal) moinsCher.get("prixUnitaire"));
        dto.setQuantiteRecommandee(quantiteRecommandee);

        return dto;

    }

}