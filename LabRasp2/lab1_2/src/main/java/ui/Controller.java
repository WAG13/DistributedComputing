package ui;

import dao.CountryDao;
import dao.CityDao;
import dao.WorldMapDao;
import entity.City;
import entity.Country;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import jdbc.dao.*;
import xml.dao.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextField countryId;
    @FXML
    private TextField countryName;
    @FXML
    private TextField cityId;
    @FXML
    private TextField cityName;
    @FXML
    private TextField cityPopulation;

    private WorldMapDao worldMapDao;
    private CityDao cityDao;
    private CountryDao countryDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**/
        worldMapDao = XmlWorldMapDao.INSTANCE;
        cityDao = XmlCityDao.INSTANCE;
        countryDao = XmlCountryDao.INSTANCE;
        /*/
        worldMapDao = JdbcWorldMapDao.INSTANCE;
        cityDao = JdbcCityDao.INSTANCE;
        countryDao = JdbcCountryDao.INSTANCE;
        /**/
        printToTreeView();
    }

    public void printToTreeView() {
        treeView.setRoot(null);
        List<Country> countries = countryDao.getCountries();
        TreeItem<String> root = new TreeItem<>("world-map");

        for (Country country : countries) {
            String info = "id: " + country.getId() + " name: " + country.getName();
            List<City> cities = country.getCities();
            TreeItem<String> countryItem = new TreeItem<>(info);

            for (City city : cities) {
                String cityInfo = "id: " + city.getId() + " name: " + city.getName() + " population: " + city.getPopulation();
                TreeItem<String> cityItem = new TreeItem<>(cityInfo);
                countryItem.getChildren().add(cityItem);
            }
            root.getChildren().add(countryItem);
        }
        treeView.setRoot(root);
    }

    @FXML
    private void addCountry(ActionEvent event) {
        Country country = new Country(countryId.getText(),countryName.getText());
        countryDao.addCountry(country);
        printToTreeView();
    }

    @FXML
    private void updateCountry(ActionEvent event) {
        TreeItem<String> selectedItem = getSelectedCountryItem();
        if (selectedItem == null) {
            return;
        }

        String newId = countryId.getText();
        String newName = countryName.getText();

        String countryId = selectedItem.getValue().split(" ")[1];
        String countryName = selectedItem.getValue().split(" ")[3];

        if (newId.equals("")) {
            newId = countryId;
        }
        if (newName.equals("")) {
            newName = countryName;
        }
        countryDao.updateCountry(Integer.valueOf(countryId), Integer.valueOf(newId), newName);
        printToTreeView();
    }

    @FXML
    public void deleteCountry(ActionEvent event) {
        TreeItem<String> selectedItem = getSelectedCountryItem();
        if (selectedItem == null) {
            return;
        }

        String countryId = selectedItem.getValue().split(" ")[1];
        countryDao.deleteCountry(Integer.valueOf(countryId));
        printToTreeView();
    }

    @FXML
    public void findCountry(ActionEvent event) {
        String countryIdValue = countryId.getText();

        Optional<Country> foundCountry = countryDao.findCountryById(Integer.parseInt(countryIdValue));
        if (foundCountry.isEmpty()) {
            return;
        }

        for (TreeItem<String> countryItem : treeView.getRoot().getChildren()) {
            String id = countryItem.getValue().split(" ")[1];
            if (id.equals(countryIdValue)) {
                treeView.getSelectionModel().select(countryItem);
                break;
            }
        }
    }

    @FXML
    private void addCity(ActionEvent event) {
        TreeItem<String> selectedItem = getSelectedCountryItem();
        if (selectedItem == null) {
            return;
        }
        String countryId = selectedItem.getValue().split(" ")[1];
        cityDao.addCity(Integer.valueOf(countryId),
                new City(cityId.getText(), cityName.getText(), cityPopulation.getText()));
        printToTreeView();
    }

    @FXML
    public void updateCity(ActionEvent event) {
        TreeItem<String> selectedItem = getSelectedCityItem();
        if (selectedItem == null) {
            return;
        }

        String newCityId = cityId.getText();
        String newName = cityName.getText();
        String newPopul = cityPopulation.getText();

        String countryId = selectedItem.getParent().getValue().split(" ")[1];
        String cityId = selectedItem.getValue().split(" ")[1];
        String cityName = selectedItem.getValue().split(" ")[3];
        String cityPopul = selectedItem.getValue().split(" ")[5];

        if (newCityId.equals("")) {
            newCityId = cityId;
        }
        if (newName.equals("")) {
            newName = cityName;
        }
        if (newPopul.equals("")) {
            newPopul = cityPopul;
        }

        cityDao.updateCity(Integer.valueOf(countryId), Integer.valueOf(cityId),
                new City(newCityId, newName, newPopul));
        printToTreeView();
    }

    @FXML
    public void deleteCity(ActionEvent event) {
        TreeItem<String> selectedItem = getSelectedCityItem();
        if (selectedItem == null) {
            return;
        }

        String countryId = selectedItem.getParent().getValue().split(" ")[1];
        String cityId = selectedItem.getValue().split(" ")[1];
        cityDao.deleteCity(Integer.valueOf(countryId), Integer.valueOf(cityId));
        printToTreeView();
    }

    @FXML
    public void findCity(ActionEvent event) {
        String cityIdValue = cityId.getText();

        Optional<City> city = cityDao.findCityById(Integer.parseInt(cityIdValue));
        if (city.isEmpty()) {
            return;
        }

        for (TreeItem<String> countryItem : treeView.getRoot().getChildren()) {
            for (TreeItem<String> cityItem : countryItem.getChildren()) {
                String id = cityItem.getValue().split(" ")[1];
                if (id.equals(cityIdValue)) {
                    treeView.getSelectionModel().select(cityItem);
                    break;
                }
            }
        }
    }

    @FXML
    public void save(ActionEvent event) {
        worldMapDao.saveWorldMap(worldMapDao.getWorldMap());
    }

    private TreeItem<String> getSelectedCountryItem() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getParent() != treeView.getRoot()) {
            createWarningMessage("Country is not selected");
            return null;
        }
        return selectedItem;
    }

    private TreeItem<String> getSelectedCityItem() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getParent() == null || selectedItem.getParent().getParent() != treeView.getRoot()) {
            createWarningMessage("City is not selected");
            return null;
        }
        return selectedItem;
    }

    private void createWarningMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(message);

        alert.showAndWait();
    }
}
