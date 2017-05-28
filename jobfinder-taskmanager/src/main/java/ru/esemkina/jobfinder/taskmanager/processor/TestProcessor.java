package ru.esemkina.jobfinder.taskmanager.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Kate on 28.05.2017.
 */
public class TestProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        exchange.getOut().setBody("dfsdfsdfsdfs");
    }
}
