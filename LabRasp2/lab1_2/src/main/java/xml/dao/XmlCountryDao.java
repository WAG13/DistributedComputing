package xml.dao;

import dao.CountryDao;
import entity.Country;

import java.util.List;
import java.util.Optional;

public enum XmlCountryDao implements CountryDao {
    INSTANCE;

    @Override
    public List<Country> getCountries() {
        return XmlWorldMapDao.INSTANCE.getWorldMap().getCountries();
    }

    @Override
    public Optional<Country> findCountryById(int countryId) {
        return XmlWorldMapDao.INSTANCE.getWorldMap().getCountries()
                .stream()
                .filter(country -> country.getId() == countryId)
                .findFirst();
    }

    @Override
    public void addCountry(Country country) {
        XmlWorldMapDao.INSTANCE.getWorldMap().addCountry(country);
    }

    @Override
    public void updateCountry(int countryId, int newCountryId, String newName) {
        Country country = findCountryById(countryId).orElseThrow();
        country.setName(newName);
        country.setId(newCountryId);
    }

    @Override
    public void deleteCountry(int countryId) {
        XmlWorldMapDao.INSTANCE.getWorldMap().getCountries()
                .removeIf(country -> country.getId() == countryId);
    }
}
