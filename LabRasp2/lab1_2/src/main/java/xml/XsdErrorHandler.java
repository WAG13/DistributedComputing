package xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        handleError("warning", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        handleError("error", exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        handleError("fatal", exception);
    }

    private void handleError(String level, SAXParseException exception) throws SAXException {
        int lineNumber = exception.getLineNumber();
        int columnNumber = exception.getColumnNumber();
        String message = exception.getMessage();
        throw new SAXException("[" + level + "] line nr: " + lineNumber + " column nr: " + columnNumber + " message: " + message);
    }
}