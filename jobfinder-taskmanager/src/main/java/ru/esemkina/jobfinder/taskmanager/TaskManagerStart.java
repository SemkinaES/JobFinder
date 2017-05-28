package ru.esemkina.jobfinder.taskmanager;

import com.mongodb.MongoClient;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.esemkina.jobfinder.taskmanager.processor.TaskManagerProcessor;

@SpringBootApplication
public class TaskManagerStart {

    @Bean
    public Datastore datastore() {
        return new Morphia().createDatastore(new MongoClient(), "local");
    }

    @Bean
    public TaskManagerProcessor taskManagerProcessor() {
        return new TaskManagerProcessor();
    }

    @Bean
    public ActiveMQComponent activemq() {
        return ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerStart.class, args);
    }

}
