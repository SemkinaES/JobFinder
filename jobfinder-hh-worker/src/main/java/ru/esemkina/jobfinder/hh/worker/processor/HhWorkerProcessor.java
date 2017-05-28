package ru.esemkina.jobfinder.hh.worker.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.hh.worker.CityManager;
import ru.esemkina.jobfinder.hh.worker.Store.Task;

/**
 * Created by Kate on 28.05.2017.
 */

public class HhWorkerProcessor implements Processor {
    @Autowired
    private Datastore datastore;
    @Autowired
    private CityManager cityManager;

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    public void process(Exchange exchange) throws Exception {
        Gson gson = new GsonBuilder().create();
        String taskString = exchange.getIn().getBody(String.class);

        Task task = gson.fromJson(taskString, Task.class);
        exchange.setProperty("task", task);

        String url = "https://api.hh.ru/vacancies?text=" + task.getQuery() + "&area=" + cityManager.findId(task.getCity());
        exchange.getOut().setBody(url);
    }
}
