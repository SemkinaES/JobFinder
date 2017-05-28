package ru.esemkina.jobfinder.web.ui.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;
import ru.esemkina.jobfinder.web.ui.simple.store.ClientManager;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Kate on 27.05.2017.
 */
@RestController
public class ClientService {

    @Autowired
    private ClientManager clientManager;

    @RequestMapping(method = POST, value = "/api/client")
    public String registerClient(@RequestBody Client client) {
        clientManager.register(client);
        return "{\"status\": \"OK\"}";
    }

    @RequestMapping(method = PUT, value = "/api/client")
    public Client updateClient(@RequestBody Client client) {
        clientManager.update(client);
        return client;
    }

    @RequestMapping(method = POST, value = "/api/client/auth")
    public Map getClient(@RequestBody HashMap<String, String> body) {
        return clientManager.get(body.get("login"), body.get("password"));
    }
}
