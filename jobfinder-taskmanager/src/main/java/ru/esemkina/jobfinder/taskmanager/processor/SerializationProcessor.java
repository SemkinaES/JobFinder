package ru.esemkina.jobfinder.taskmanager.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import ru.esemkina.jobfinder.taskmanager.Store.Task;

/**
 * Created by Kate on 28.05.2017.
 */
public class SerializationProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Task body = exchange.getIn().getBody(Task.class);
        Gson gson = new GsonBuilder().create();
        exchange.getOut().setBody(gson.toJson(body));
    }
}
