package ru.esemkina.jobfinder.notifier;

import com.mongodb.MongoClient;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.esemkina.jobfinder.notifier.processor.*;

@SpringBootApplication
public class NotifierStart {
    @Bean
    public EmailMessageProcessor emailMessageProcessor() {
        return new EmailMessageProcessor();
    }

    @Bean
    public Datastore datastore() {
        return new Morphia().createDatastore(new MongoClient(), "local");
    }

    @Bean
    public DeserializationProcessor deserializationProcessor() {
        return new DeserializationProcessor();
    }

    @Bean
    public SerializationProcessor serializationProcessor() {
        return new SerializationProcessor();
    }

    @Bean
    public FindClientProcessor findClientProcessor() {
        return new FindClientProcessor();
    }

    @Bean
    public HtmlCreatorProcessor htmlCreatorProcessor() {
        return new HtmlCreatorProcessor();
    }

    @Bean
    public ActiveMQComponent activemq() {
        return ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
    }

    public static void main(String[] args) {
        SpringApplication.run(NotifierStart.class, args);
    }

}
