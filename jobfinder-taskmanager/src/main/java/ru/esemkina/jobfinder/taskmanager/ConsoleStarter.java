package ru.esemkina.jobfinder.taskmanager;

import com.mongodb.MongoClient;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import ru.esemkina.jobfinder.taskmanager.Store.Task;
import ru.esemkina.jobfinder.taskmanager.processor.TaskManagerProcessor;

/**
 * Created by Kate on 28.05.2017.
 */
public class ConsoleStarter {

    public static void main(String... arg) throws Exception {

        Datastore local = new Morphia().createDatastore(new MongoClient(), "local");

        TaskManagerProcessor taskManagerProcessor = new TaskManagerProcessor();
        taskManagerProcessor.setDatastore(local);

        DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
        DefaultExchange defaultExchange = new DefaultExchange(defaultCamelContext);

        //run processor
        taskManagerProcessor.process(defaultExchange);

        Object body = defaultExchange.getOut().getBody();
        System.out.println(body);
    }
}
