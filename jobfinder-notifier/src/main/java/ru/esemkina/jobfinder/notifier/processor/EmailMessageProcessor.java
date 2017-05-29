package ru.esemkina.jobfinder.notifier.processor;

import com.sun.xml.bind.v2.runtime.output.Encoded;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kate on 28.05.2017.
 */
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
