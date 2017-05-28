package ru.esemkina.jobfinder.hh.worker.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.hh.worker.Store.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Kate on 28.05.2017.
 */
public class ResultTaskProcessor implements Processor {
    @Autowired
    private Datastore dataStore;

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
            List<Map> newTasks = new ArrayList<>();
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
                    newTasks.add(x);
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
