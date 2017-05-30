package ru.esemkina.jobfinder.indeed.worker;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esemkina.jobfinder.indeed.worker.processor.*;

@Component
public class IndeedWorkerRouter extends RouteBuilder {

    @Autowired
    private ResultTaskProcessor resultTaskProcessor;
    @Autowired
    private IndeedWorkerProcessor indeedWorkerProcessor;
    @Autowired
    private DeserializationProcessor deserializationProcessor;
    @Autowired
    private ResultProcessor resultProcessor;
    @Autowired
    private SerializationProcessor serializationProcessor;

    @Override
    public void configure() {
        from("activemq:queue://tasks" +
                "?username=admin" +
                "&password=admin" +
                "&disableReplyTo=true")
                .process(indeedWorkerProcessor)
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
