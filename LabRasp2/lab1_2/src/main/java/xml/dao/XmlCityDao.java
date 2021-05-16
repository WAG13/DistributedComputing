package xml.dao;

import dao.CityDao;
import entity.City;
import entity.Country;

import java.util.Optional;

public enum XmlCityDao implements CityDao {
    INSTANCE;

    @Override
    public void addCity(int countryId, City city) {
        Country country = XmlCountryDao.INSTANCE.findCountryById(countryId).orElseThrow();
        country.addCity(city);
    }

    @Override
    public void updateCity(int countryId, int cityId, City newCity) {
        Country country = XmlCountryDao.INSTANCE.findCountryById(countryId).orElseThrow();
        country.getCities().stream()
                .filter(c -> c.getId() == cityId)
                .findFirst()
                .ifPresent(c -> {
                    c.setId(newCity.getId());
                    c.setName(newCity.getName());
                    c.setPopulation(newCity.getPopulation());
                });
    }

    @Override
    public void deleteCity(int countryId, int cityId) {
        Country country = XmlCountryDao.INSTANCE.findCountryById(countryId).orElseThrow();
        country.getCities().removeIf(city -> city.getId() == cityId);
    }

    @Override
    public Optional<City> findCityById(int cityId) {
        return XmlCountryDao.INSTANCE.getCountries()
                .stream()
                .flatMap(country -> country.getCities().stream())
                .filter(city -> city.getId() == cityId)
                .findFirst();
    }
}
