package ru.esemkina.jobfinder.notifier.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SerializationProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        Gson gson = new GsonBuilder().create();
        exchange.getOut().setBody(gson.toJson(body));
    }
}
