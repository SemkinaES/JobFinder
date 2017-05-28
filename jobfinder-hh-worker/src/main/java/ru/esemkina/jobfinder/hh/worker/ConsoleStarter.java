package ru.esemkina.jobfinder.hh.worker;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Date;

/**
 * Created by Kate on 28.05.2017.
 */
public class ConsoleStarter {

    public static void main(String... arg) throws Exception {
        Datastore local = new Morphia().createDatastore(new MongoClient(), "local");

        CityManager cm = new CityManager();
        cm.setDatastore(local);
        cm.init();
        String tyumen = cm.findId("Тюмень4");
        System.out.println(tyumen);
//        HhWorkerProcessor hhWorkerProcessor = new HhWorkerProcessor();
//        hhWorkerProcessor.setDatastore(local);
//
//        DefaultCamelContext defaultCamelContext = new DefaultCamelContext();
//        DefaultExchange defaultExchange = new DefaultExchange(defaultCamelContext);
//
//        //run processor
//        hhWorkerProcessor.process(defaultExchange);
//
//        Object body = defaultExchange.getOut().getBody();
//        System.out.println(body);
    }
}
