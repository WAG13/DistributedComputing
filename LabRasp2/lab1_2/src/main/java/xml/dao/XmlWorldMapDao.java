package xml.dao;

import dao.WorldMapDao;
import entity.City;
import entity.Country;
import entity.WorldMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public enum XmlWorldMapDao implements WorldMapDao {
    INSTANCE;

    private static final String XML_FILE = "src/main/resources/xml/file.xml";

    private WorldMap worldMap;

    XmlWorldMapDao() {
        worldMap = load(XML_FILE);
    }

    private WorldMap load(String file) {
        WorldMap worldMap = new WorldMap();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("country");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element countryElement = (Element) nNode;
                    Country country = new Country(Integer.valueOf(countryElement.getAttribute("id")),
                            countryElement.getAttribute("name"));
                    worldMap.addCountry(country);
                    NodeList cityNodeList = countryElement.getElementsByTagName("city");

                    for (int j = 0; j < cityNodeList.getLength(); j++) {
                        Node cityNode = cityNodeList.item(j);

                        if (cityNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element cityElement = (Element) cityNode;
                            City city = new City(Integer.valueOf(cityElement.getAttribute("id")),
                                    cityElement.getAttribute("name"), cityElement.getAttribute("population"));
                            country.addCity(city);
                        }
                    }

                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Load can't be completed.");
        }
        this.worldMap = worldMap;
        return worldMap;
    }

    private void persist(WorldMap worldMap, String file) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("world-map");
            document.appendChild(root);
            for (Country country : worldMap.getCountries()) {
                Element countryElement = document.createElement("country");
                countryElement.setAttribute("id", String.valueOf(country.getId()));
                countryElement.setAttribute("name", country.getName());
                for (City city : country.getCities()) {
                    Element cityElement = document.createElement("city");
                    cityElement.setAttribute("id", String.valueOf(city.getId()));
                    cityElement.setAttribute("name", city.getName());
                    cityElement.setAttribute("population", city.getPopulation());
                    countryElement.appendChild(cityElement);
                }
                root.appendChild(countryElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(file));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("Persist operation can't be completed.");
        }
    }

    @Override
    public WorldMap getWorldMap() {
        return worldMap;
    }

    @Override
    public void saveWorldMap(WorldMap worldMap) {
        persist(worldMap, XML_FILE + ".saved");
    }
}
