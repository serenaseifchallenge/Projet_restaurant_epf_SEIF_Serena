package fr.epf.restaurant.controller;

import fr.epf.restaurant.service.CommandeClientService;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.model.CommandeClient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/commandes/client")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeClientController {

    private final CommandeClientService service;

    public CommandeClientController(CommandeClientService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommandeClient> getAll(@RequestParam(required = false) String statut) {
        return service.findAll(statut);
    }

    @GetMapping("/{id}")
    public CommandeClient getDetailCommandeClient(@PathVariable("id") long commandeId) {
        return service.findDetailCommandeClient(commandeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeClient creer(@RequestBody CreerCommandeClientRequest creerCommandeClientRequest) {
        return service.creerCommande(creerCommandeClientRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/preparer")
    public void preparer(@PathVariable long id) {
        service.preparer(id);
    }

    @PutMapping("/{id}/servir")
    public void servir(@PathVariable long id) {
        service.servir(id);
    }
}