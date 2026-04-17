package fr.epf.restaurant.controller;

import fr.epf.restaurant.model.Plat;
import fr.epf.restaurant.dao.PlatDao;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plats")
@CrossOrigin(origins = "http://localhost:4200")
public class PlatController {

    private final PlatDao platDao;

    public PlatController(PlatDao platDao) {
        this.platDao = platDao;
    }

    @GetMapping
    public List<Plat> getAll() {
        return platDao.findAll();
    }

    @GetMapping("/{id}")
    public Plat getPlat(@PathVariable long id) {
        return platDao.findById(id);
    }

}