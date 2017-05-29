package ru.esemkina.jobfinder.notifier.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;

public class DeserializationProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        Gson gson = new GsonBuilder().create();
        exchange.getOut().setBody(gson.fromJson(body, HashMap.class));
    }
}
