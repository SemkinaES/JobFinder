package ru.esemkina.jobfinder.taskmanager.Store;

/**
 * Created by Kate on 28.05.2017.
 */
public class Task {
    private String query;
    private String city;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        return (query + city).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
