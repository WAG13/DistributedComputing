package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String population;

    public City(String id, String name, String population){
        this.name = name;
        this.population = population;
        this.id = (id.isEmpty()) ? 0 : Integer.parseInt(id);
    }
}
