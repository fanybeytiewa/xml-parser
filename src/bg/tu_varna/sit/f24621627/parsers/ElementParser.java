package bg.tu_varna.sit.f24621627.parsers;

import bg.tu_varna.sit.f24621627.models.XmlElement;
import bg.tu_varna.sit.f24621627.exceptions.XmlParseException;

/**
 * Common interface for XML element parsers.
 * Defines a contract for parsing a string into an {@link XmlElement}.
 *
 * Implementations:
 * <ul>
 *     <li>{@link SimpleTagParser} — parses a tag with no attributes</li>
 *     <li>{@link AttributeTagParser} — parses a tag with attributes</li>
 *     <li>{@link XmlParser} — parses a full XML string into a tree with children</li>
 * </ul>
 */
public interface ElementParser {

    /**
     * Parses the given content and returns an XmlElement.
     * @param content the content to parse
     * @return the parsed element
     * @throws XmlParseException if the content is invalid
     */
    XmlElement parse(String content) throws XmlParseException;
}
