package ru.esemkina.jobfinder.notifier.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindClientProcessor implements Processor {
    @Autowired
    public Datastore datastore;

    @Override
    public void process(Exchange exchange) throws Exception {
        HashMap vacancies = exchange.getIn().getBody(HashMap.class);
        Map task = (Map) vacancies.get("task");

        String query = (String) task.get("query");
        String city = (String) task.get("city");

        List<Client> clients = datastore.createQuery(Client.class)
                .field("city")
                .equal(city)
                .field("queries")
                .hasThisOne(query)
                .asList();

        ArrayList<HashMap> result = new ArrayList<>();
        clients.forEach(x -> {
            HashMap<String, Object> item = new HashMap<>();
            item.put("client", x);
            item.put("vacancies", vacancies);
            result.add(item);
        });
        exchange.getOut().setBody(result);
    }
}
