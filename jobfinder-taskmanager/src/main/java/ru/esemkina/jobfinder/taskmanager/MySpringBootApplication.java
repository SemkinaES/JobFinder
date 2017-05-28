package ru.esemkina.jobfinder.taskmanager;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MySpringBootApplication {

    @Bean
    public ActiveMQComponent activemq() {
        return ActiveMQComponent
                .activeMQComponent("tcp://localhost:61616");
    }
    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

}
