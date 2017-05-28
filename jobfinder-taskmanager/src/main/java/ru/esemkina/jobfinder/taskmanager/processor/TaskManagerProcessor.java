package ru.esemkina.jobfinder.taskmanager.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;
import ru.esemkina.jobfinder.taskmanager.Store.Task;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Kate on 28.05.2017.
 */

public class TaskManagerProcessor implements Processor {
    @Autowired
    private Datastore datastore;

    public void process(Exchange exchange) throws Exception {
        List<Client> clients = datastore.createQuery(Client.class).asList();
        HashSet<Task> tasks = new HashSet<Task>();
        clients.forEach(x -> {
            x.getQueries().forEach(y -> {
                Task newTask = new Task();
                newTask.setCity(x.getCity());
                newTask.setQuery(y);
                tasks.add(newTask);
            });
        });
        exchange.getOut().setBody(tasks);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }
}
