package ru.esemkina.jobfinder.indeed.worker.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import ru.esemkina.jobfinder.indeed.worker.Store.Task;

public class IndeedWorkerProcessor implements Processor {

    @Value("${indeed.token}")
    private String indeedToken;

    public void process(Exchange exchange) throws Exception {
        Gson gson = new GsonBuilder().create();
        String taskString = exchange.getIn().getBody(String.class);

        Task task = gson.fromJson(taskString, Task.class);
        exchange.setProperty("task", task);

        String url = "http://api.indeed.com/ads/apisearch?publisher=" + indeedToken + "&q=" + task.getQuery() +
                "&co=ru&l=" + task.getCity() + "&v=2&format=json";
        exchange.getOut().setBody(url);
    }
}
