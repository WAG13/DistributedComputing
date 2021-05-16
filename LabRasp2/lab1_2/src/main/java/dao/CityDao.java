package dao;

import entity.City;

import java.util.Optional;

public interface CityDao {

    void addCity(int countryId, City city);

    void updateCity(int countryId, int cityId, City newCity);

    void deleteCity(int countryId, int cityId);

    Optional<City> findCityById(int cityId);
}
