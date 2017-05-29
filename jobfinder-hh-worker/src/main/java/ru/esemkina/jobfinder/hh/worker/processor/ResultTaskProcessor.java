package ru.esemkina.jobfinder.hh.worker.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.hh.worker.Store.Task;
import ru.esemkina.jobfinder.hh.worker.Store.Vacancy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResultTaskProcessor implements Processor {
    @Autowired
    private Datastore dataStore;

    private Vacancy vacancyCreator(Map map) {
        Vacancy vacancy = new Vacancy();
        vacancy.setName((String) map.get("name"));
        Map snippet = (Map) map.get("snippet");
        vacancy.setResponsibility((String) snippet.get("responsibility"));
        vacancy.setRequirement((String) snippet.get("requirement"));
        Map area = (Map) map.get("area");
        vacancy.setCity((String) area.get("name"));
        vacancy.setUrl((String) map.get("alternate_url"));
        vacancy.setCreatedAt((String) map.get("created_at"));
        Map employer = (Map) map.get("employer");
        vacancy.setEmployerName((String) employer.get("name"));
        return vacancy;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Task task = (Task) exchange.getProperty("task");

        Query<Task> storedTaskQuery = dataStore.createQuery(Task.class)
                .field("site")
                .equal("hh")
                .field("query")
                .equal(task.getQuery())
                .field("city")
                .equal(task.getCity());

        List<Task> tasks = storedTaskQuery.asList();

        if (tasks.size() == 0) {
            task.setSite("hh");
            task.setDate(new Date());
            dataStore.save(task);
            exchange.getOut().setBody(new ArrayList<>());
        } else {
            Task storedTask = tasks.get(0);
            Date maxDate = storedTask.getDate();
            List<Vacancy> newTasks = new ArrayList<>();
            HashMap body = exchange.getIn().getBody(HashMap.class);
            List<Map> items = (List) body.get("items");

            for (Map x : items) {
                //get date
                String created_at = (String) x.get("created_at");
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
                Date itemDate = null;
                try {
                    itemDate = formatter.parse(created_at);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (itemDate.after(storedTask.getDate())) {
                    newTasks.add(vacancyCreator(x));
                    if (itemDate.after(maxDate)) {
                        maxDate = itemDate;
                    }
                }
            }

            exchange.getOut().setBody(newTasks);

            if (maxDate.after(storedTask.getDate())) {
                UpdateOperations<Task> updateStoredTask = dataStore
                        .createUpdateOperations(Task.class)
                        .set("date", maxDate);
                dataStore.updateFirst(storedTaskQuery, updateStoredTask);
            }
        }
    }
}
