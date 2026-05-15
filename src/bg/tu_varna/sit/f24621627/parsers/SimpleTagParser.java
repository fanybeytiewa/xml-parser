package bg.tu_varna.sit.f24621627.parsers;

import bg.tu_varna.sit.f24621627.models.XmlElement;

/**
 * Parses a simple XML tag without attributes.
 * Handles tags like {@code <title>}, {@code <author>}, {@code <name>}.
 * Returns an XmlElement with only a tag name, no attributes.
 */
public class SimpleTagParser implements ElementParser {

    /**
     * Creates an XmlElement from a tag name with no attributes.
     * @param tagName the tag name (e.g. "title")
     * @return a new element with the given tag name
     */
    @Override
    public XmlElement parse(String tagName) {
        return new XmlElement(tagName.trim());
    }
}
