package xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlValidator {

    private static final String XSD_SCHEMA = "src/main/resources/xml/world-map.xsd";

    public static boolean isValidWorldMap(File file) throws SAXException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(XSD_SCHEMA));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new XsdErrorHandler());
            validator.validate(new StreamSource(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error opening file " + file, e);
        }
        return true;
    }
}
