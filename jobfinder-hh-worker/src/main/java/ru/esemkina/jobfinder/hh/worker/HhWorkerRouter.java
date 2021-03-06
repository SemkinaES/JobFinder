package ru.esemkina.jobfinder.hh.worker;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esemkina.jobfinder.hh.worker.processor.*;

@Component
public class HhWorkerRouter extends RouteBuilder {

    @Autowired
    private ResultTaskProcessor resultTaskProcessor;
    @Autowired
    private HhWorkerProcessor hhWorkerProcessor;
    @Autowired
    private DeserializationProcessor deserializationProcessor;
    @Autowired
    private ResultProcessor resultProcessor;
    @Autowired
    private SerializationProcessor serializationProcessor;
    @Autowired
    private CityManager cityManager;

    @Override
    public void configure() {
        cityManager.init();
        from("activemq:topic://tasks" +
                "?username=admin" +
                "&password=admin" +
                "&disableReplyTo=true")
                .process(hhWorkerProcessor)
                .recipientList(body())
                .process(deserializationProcessor)
                .process(resultTaskProcessor)
                .choice()
                    .when(simple("${body.size} > 0"))
                        .to("direct:send")
                    .otherwise()
                        .log("Nothing to do");

        from("direct:send")
                .process(resultProcessor)
                .process(serializationProcessor)
                .to("activemq:queue://results" +
                        "?username=admin" +
                        "&password=admin" +
                        "&disableReplyTo=true");
    }
}
