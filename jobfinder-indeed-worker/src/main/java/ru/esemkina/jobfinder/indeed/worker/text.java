package ru.esemkina.jobfinder.indeed.worker;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.indeed.worker.Store.Task;
import ru.esemkina.jobfinder.indeed.worker.Store.Vacancy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kate on 30.05.2017.
 */
public class text {
    @Autowired
    private static Datastore dataStore = new Morphia().createDatastore(new MongoClient(), "local");

    public static void main(String... arg) throws ParseException {
        Query<Task> storedTaskQuery = dataStore.createQuery(Task.class)
                .field("site")
                .equal("indeed")
                .field("query")
                .equal("java")
                .field("city")
                .equal("Тюмень");

        List<Task> tasks = storedTaskQuery.asList();

        if (tasks.size() == 0) {
            int x = 0;
        } else {
            Task storedTask = tasks.get(0);
            Date maxDate = storedTask.getDate();
            List<Vacancy> newTasks = new ArrayList<>();
        }
    }
}
