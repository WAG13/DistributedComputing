package jdbc.dao;

import dao.CityDao;
import entity.City;
import jdbc.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public enum JdbcCityDao implements CityDao {
    INSTANCE;

    @Override
    public void addCity(int countryId, City city) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "INSERT INTO city (name, population, country_id) VALUES (?, ?, ?)"
        )) {
            statement.setString(1, city.getName());
            statement.setString(2, city.getPopulation());
            statement.setInt(3, countryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCity(int countryId, int cityId, City newCity) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "UPDATE city SET id = ?, name = ?, population = ? WHERE id = ?"
        )) {
            statement.setInt(1, newCity.getId());
            statement.setString(2, newCity.getName());
            statement.setString(3, newCity.getPopulation());
            statement.setInt(4, cityId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCity(int countryId, int cityId) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "DELETE FROM city WHERE id = ?"
        )) {
            statement.setInt(1, cityId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<City> findCityById(int cityId) {
        try (PreparedStatement statement = DatabaseConnection.INSTANCE.getConnection().prepareStatement(
                "SELECT id, name, population FROM city WHERE id = ?"
        )) {
            statement.setInt(1, cityId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new City(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
