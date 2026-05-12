package bg.tu_varna.sit.f24621627;

/**
 * Exception for XML parsing errors.
 * Thrown when the XML structure is invalid
 * (e.g. mismatched tags, missing opening tag).
 */
public class XmlParseException extends Exception {

    public XmlParseException(String message) {
        super(message);
    }
}
