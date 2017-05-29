package ru.esemkina.jobfinder.hh.worker;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import ru.esemkina.jobfinder.hh.worker.Store.City;

import java.util.List;

public class CityManager {

    @Autowired
    private Datastore datastore;
    private List<City> cities;

    public String findId(String city) {
        return findRecursive(cities, city);
    }

    private String findRecursive(List<City> cities, String name) {
        final String[] result = {null};

        cities.forEach(x -> {
            if (x.getName().equals(name)) {
                result[0] = x.getId();
            } else if (x.getAreas() != null) {
                String recursive = findRecursive(x.getAreas(), name);
                if (recursive != null) {
                    result[0] = recursive;
                }
            }
        });

        return result[0];
    }

    public void init() {
        cities = datastore.createQuery(City.class).asList();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }
}

