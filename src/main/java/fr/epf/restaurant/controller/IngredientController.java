package fr.epf.restaurant.controller;

import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.service.StockService;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.RecommandationDto;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:4200")
public class IngredientController {

    private final IngredientDao ingredientDao;
    private final StockService stockService;

    public IngredientController(IngredientDao ingredientDao, StockService stockService) {
        this.ingredientDao = ingredientDao;
        this.stockService = stockService;
    }

    @GetMapping
    public List<Ingredient> getAll() {
        return ingredientDao.findAll();
    }

    @GetMapping("/alertes")
    public List<AlerteStockDto> getAlertes() {
        return stockService.findIngredientsSousAlerte();
    }

    @GetMapping("/{id}/prix")
    public List<Map<String, Object>> getPrix(@PathVariable long id) {
        return stockService.getPrix(id);
    }

    @GetMapping("/{id}/recommandation")
    public RecommandationDto getFournisseurMoinsCher(@PathVariable long id) {
        return stockService.getMoinsCher(id);
    }

}