package ru.esemkina.jobfinder.web.ui.simple.store;

import com.mongodb.DBObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;

import java.util.Map;

/**
 * Created by Kate on 27.05.2017.
 */
public class ClientManager {

    @Autowired
    private Datastore datastore;

    public void register(Client client) {
        datastore.save(client);
    }

    public void update(Client client) {
        Query<Client> selectUser = datastore.createQuery(Client.class)
                .field("password")
                .equal(client.getPassword())
                .field("login")
                .equal(client.getLogin());

        UpdateOperations<Client> updateUser = datastore.createUpdateOperations(Client.class)
                .set("name", client.getName())
                .set("email", client.getEmail())
                .set("city", client.getCity())
                .set("queries", client.getQueries())
                .set("login", client.getLogin())
                .set("password", client.getPassword());

        datastore.update(selectUser, updateUser);
    }
    public Map get(String login, String password) {
        Query<Client> selectUser = datastore.createQuery(Client.class)
                .field("password")
                .equal(password)
                .field("login")
                .equal(login);

        DBObject user = datastore.getCollection(Client.class).findOne(selectUser.getQueryObject());

        return user.toMap();
    }
}
