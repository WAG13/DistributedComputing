package jdbc.dao;

import dao.CountryDao;
import entity.Country;
import entity.City;
import jdbc.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum JdbcCountryDao implements CountryDao {
    INSTANCE;

    @Override
    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();

        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "SELECT id, name FROM country"
        )) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int countryId = resultSet.getInt(1);
                countries.add(new Country(
                        countryId,
                        resultSet.getString(2),
                        getCitiesByCountryId(countryId)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    @Override
    public Optional<Country> findCountryById(int countryId) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "SELECT id, name FROM country WHERE id = ?"
        )) {
            statement.setInt(1, countryId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Country country = new Country(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        getCitiesByCountryId(countryId)
                );
                return Optional.of(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void addCountry(Country country) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "INSERT INTO country (name) VALUES (?)"
        )) {
            statement.setString(1, country.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCountry(int countryId, int newCountryId, String newName) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "UPDATE country SET id = ?, name = ? WHERE id = ?"
        )) {
            statement.setInt(1, newCountryId);
            statement.setString(2, newName);
            statement.setInt(3, countryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCountry(int countryId) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "DELETE FROM city WHERE country_id = ?"
        )) {
            statement.setInt(1, countryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "DELETE FROM country WHERE id = ?"
        )) {
            statement.setInt(1, countryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<City> getCitiesByCountryId(int countryId) {
        List<City> cities = new ArrayList<>();

        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "SELECT m.id, m.name, m.population FROM city m WHERE m.country_id = ?"
        )) {
            statement.setInt(1, countryId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cities.add(new City(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }
}
