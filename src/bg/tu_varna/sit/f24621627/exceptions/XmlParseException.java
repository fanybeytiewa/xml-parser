package bg.tu_varna.sit.f24621627.exceptions;

/**
 * Exception for XML parsing errors.
 * Thrown when the XML structure is invalid
 * (e.g. mismatched tags, missing opening tag).
 */
public class XmlParseException extends Exception {

    /**
     * Creates a new XML parse exception with the given message.
     * @param message the detail message describing the parsing error
     */
    public XmlParseException(String message) {
        super(message);
    }
}
