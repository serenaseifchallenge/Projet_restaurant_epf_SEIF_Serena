package fr.epf.restaurant.controller;

import fr.epf.restaurant.model.Fournisseur;
import fr.epf.restaurant.dao.FournisseurDao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fournisseurs")
@CrossOrigin(origins = "http://localhost:4200")
public class FournisseurController {

    private final FournisseurDao fournisseurDao;

    public FournisseurController(FournisseurDao fournisseurDao) {
        this.fournisseurDao = fournisseurDao;
    }

    @GetMapping
    public List<Fournisseur> getAll() {
        return fournisseurDao.findAll();
    }

    @GetMapping("/{id}/catalogue")
    public List<Map<String, Object>> getCatalogue(@PathVariable long id) {
        return fournisseurDao.findCatalogueByFournisseurId(id);
    }

}