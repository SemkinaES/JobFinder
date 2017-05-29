package ru.esemkina.jobfinder.notifier;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.esemkina.jobfinder.notifier.processor.*;

@Component
public class NotifierRouter extends RouteBuilder {

    @Autowired
    private EmailMessageProcessor emailMessageProcessor;
    @Autowired
    private DeserializationProcessor deserializationProcessor;
    @Autowired
    private SerializationProcessor serializationProcessor;
    @Autowired
    private FindClientProcessor findClientProcessor;
    @Autowired
    private HtmlCreatorProcessor htmlCreatorProcessor;

    @Override
    public void configure() {
        from("activemq:queue://results" +
                "?username=admin" +
                "&password=admin" +
                "&disableReplyTo=true")
                .setProperty("vacanciesTemplate").simple("resource:classpath:template/vacancies.html")
                .setProperty("vacancyTemplate").simple("resource:classpath:template/vacancyItem.html")
                .process(deserializationProcessor)
                .process(findClientProcessor)
                .split(body())
                .process(htmlCreatorProcessor)
                .process(emailMessageProcessor)
                .to("smtps://smtp.gmail.com" +
                        "?port=465" +
                        "&username=job.finder.application@gmail.com" +
                        "&password=qazwsxedc123!");
    }
}
