package ru.esemkina.jobfinder.taskmanager;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.esemkina.jobfinder.taskmanager.processor.TestProcessor;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("quartz2://taskcreator?cron=0+*/1+*+*+*+?")
                .process(new TestProcessor())
                .to("activemq:topic://someName" +
                        "?username=admin" +
                        "&password=admin" +
                        "&disableReplyTo=true");

    }
}
