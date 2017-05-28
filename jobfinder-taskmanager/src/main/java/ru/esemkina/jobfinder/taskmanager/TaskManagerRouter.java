package ru.esemkina.jobfinder.taskmanager;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esemkina.jobfinder.taskmanager.processor.SerializationProcessor;
import ru.esemkina.jobfinder.taskmanager.processor.TaskManagerProcessor;

@Component
public class TaskManagerRouter extends RouteBuilder {

    @Autowired
    private TaskManagerProcessor taskManagerProcessor;

    @Override
    public void configure() {
        from("quartz2://taskcreator?cron=0+*/1+*+*+*+?")
                .process(taskManagerProcessor)
                .split(body())
                .process(new SerializationProcessor())
                .to("activemq:queue://tasks" +
                        "?username=admin" +
                        "&password=admin" +
                        "&disableReplyTo=true");

    }
}
