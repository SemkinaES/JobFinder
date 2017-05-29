package ru.esemkina.jobfinder.notifier.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;

import java.util.HashMap;
import java.util.Map;

public class EmailMessageProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();

        Client client = (Client) exchange.getProperty("client");

        //mail headers
        headers.put("To", client.getEmail());
        headers.put("From", "job.finder.application@gmail.com");
        headers.put("Subject", "An new vacancies");
        headers.put("Content-Type", "text/html;charset=UTF-8");

        String body = exchange.getIn().getBody().toString();

        exchange.getOut().setHeaders(headers);
        exchange.getOut().setBody(body);
    }
}
