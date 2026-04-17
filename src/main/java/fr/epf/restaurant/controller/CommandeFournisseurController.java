package fr.epf.restaurant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.service.CommandeFournisseurService;

@RestController
@RequestMapping("/api/commandes/fournisseur")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeFournisseurController {
    private final CommandeFournisseurService service;

    public CommandeFournisseurController(CommandeFournisseurService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommandeFournisseur> getAll() {
        return service.findAll(null);
    }

    @GetMapping("/{id}")
    public CommandeFournisseur getDetailCommandeFournisseur(@PathVariable("id") long fournisseurId) {
        return service.findDetailCommandeFournisseur(fournisseurId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeFournisseur creer(@RequestBody CreerCommandeFournisseurRequest creerCommandeFournisseurRequest) {
        return service.creerCommande(creerCommandeFournisseurRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/envoyer")
    public void envoyer(@PathVariable long id) {
        service.envoyer(id);
    }

    @PutMapping("/{id}/recevoir")
    public void recevoir(@PathVariable long id) {
        service.recevoir(id);
    }
}
