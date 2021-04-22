package entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Country {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    private List<City> cities;

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public Country(String id, String name) {
        this.name = name;
        this.id = (id.isEmpty()) ? 0 : Integer.parseInt(id);
        this.cities = new ArrayList<>();
    }

    public Country(int id, String name, List<City> cities) {
        this.id = id;
        this.name = name;
        this.cities = cities;
    }

    public Country() {
        this.cities = new ArrayList<>();
    }

    public void addCity(City city) {
        cities.add(city);
    }
}
