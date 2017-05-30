package ru.esemkina.jobfinder.indeed.worker.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.indeed.worker.Store.Task;
import ru.esemkina.jobfinder.indeed.worker.Store.Vacancy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResultTaskProcessor implements Processor {
    @Autowired
    private Datastore dataStore;

    private Vacancy vacancyCreator(Map map) {
        Vacancy vacancy = new Vacancy();
        vacancy.setName((String) map.get("jobtitle"));
        vacancy.setResponsibility((String) map.get("snippet"));
        vacancy.setCity((String) map.get("city"));
        vacancy.setUrl((String) map.get("url"));
        vacancy.setCreatedAt((String) map.get("date"));
        vacancy.setEmployerName((String) map.get("company"));
        return vacancy;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Task task = (Task) exchange.getProperty("task");

        Query<Task> storedTaskQuery = dataStore.createQuery(Task.class)
                .field("site")
                .equal("indeed")
                .field("query")
                .equal(task.getQuery())
                .field("city")
                .equal(task.getCity());

        List<Task> tasks = storedTaskQuery.asList();

        if (tasks.size() == 0) {
            task.setSite("indeed");
            task.setDate(new Date());
            dataStore.save(task);
            exchange.getOut().setBody(new ArrayList<>());
        } else {
            Task storedTask = tasks.get(0);
            Date maxDate = storedTask.getDate();
            List<Vacancy> newTasks = new ArrayList<>();
            HashMap body = exchange.getIn().getBody(HashMap.class);
            List<Map> items = (List) body.get("results");

            for (Map x : items) {
                //get date
                String created_at = (String) x.get("date");
                DateFormat formatter = new SimpleDateFormat("E',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
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
