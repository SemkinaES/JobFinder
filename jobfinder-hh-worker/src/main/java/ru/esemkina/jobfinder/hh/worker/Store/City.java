package ru.esemkina.jobfinder.hh.worker.Store;

import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by Kate on 28.05.2017.
 */
@Entity("areas")
public class City {
    private String parent_id;
    private List<City> areas;
    private String id;
    private String name;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<City> getAreas() {
        return areas;
    }

    public void setAreas(List<City> areas) {
        this.areas = areas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
