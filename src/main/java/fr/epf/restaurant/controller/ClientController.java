package fr.epf.restaurant.controller;

import fr.epf.restaurant.model.Client;
import fr.epf.restaurant.dao.ClientDao;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    private final ClientDao clientDao;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @GetMapping
    public List<Client> getAll() {
        return clientDao.findAll();
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        return clientDao.create(client);
    }

}