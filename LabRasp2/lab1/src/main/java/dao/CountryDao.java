package dao;

import entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryDao {

    void addCountry(Country country);

    void updateCountry(int countryId, int newCountryId, String newName);

    void deleteCountry(int countryId);

    Optional<Country> findCountryById(int countryId);

    List<Country> getCountries();

}
