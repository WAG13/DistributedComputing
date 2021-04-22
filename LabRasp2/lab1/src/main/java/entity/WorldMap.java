package entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WorldMap {

    @Getter
    private List<Country> countries;

    public WorldMap() {
        this.countries = new ArrayList<>();
    }

    public void addCountry(Country country) {
        countries.add(country);
    }
}
