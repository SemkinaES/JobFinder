package ru.esemkina.jobfinder.hh.worker.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;

public class ResultProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        HashMap<String, Object> result = new HashMap<>();
        result.put("task", exchange.getProperty("task"));
        result.put("vacancies", body);
        exchange.getOut().setBody(result);
    }
}
