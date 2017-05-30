package ru.esemkina.jobfinder.indeed.worker;

import com.mongodb.MongoClient;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.esemkina.jobfinder.indeed.worker.processor.*;

@SpringBootApplication
public class IndeedWorkerStart {

    @Bean
    public Datastore datastore() {
        return new Morphia().createDatastore(new MongoClient(), "local");
    }

    @Bean
    public IndeedWorkerProcessor hhWorkerProcessor() {
        return new IndeedWorkerProcessor();
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
    public ResultProcessor resultProcessor() {
        return new ResultProcessor();
    }

    @Bean
    public ResultTaskProcessor resultTaskProcessor() {
        return new ResultTaskProcessor();
    }

    @Bean
    public ActiveMQComponent activemq() {
        return ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
    }

    public static void main(String[] args) {
        SpringApplication.run(IndeedWorkerStart.class, args);
    }

}
