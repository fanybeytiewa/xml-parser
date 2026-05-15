package bg.tu_varna.sit.f24621627.parsers;

import bg.tu_varna.sit.f24621627.models.XmlElement;

/**
 * Parses an XML tag that contains attributes.
 * Handles tags like {@code <book id="1" year="1965">}.
 * Extracts the tag name and all key="value" attribute pairs.
 */
public class AttributeTagParser implements ElementParser {

    /**
     * Creates an XmlElement from a tag content string with attributes.
     * @param tagContent the content between &lt; and &gt; (e.g. "book id=\"1\" year=\"1965\"")
     * @return the created element with parsed tag name and attributes
     */
    @Override
    public XmlElement parse(String tagContent) {
        int firstSpaceIndex = tagContent.indexOf(' ');

        String tagName = tagContent.substring(0, firstSpaceIndex);
        String attributesText = tagContent.substring(firstSpaceIndex + 1).trim();

        XmlElement element = new XmlElement(tagName);
        parseAttributes(element, attributesText);

        return element;
    }

    /**
     * Parses attribute pairs from a string and adds them to the element.
     * Handles the format key="value" with multiple attributes separated by spaces.
     * @param element the element to add attributes to
     * @param attributesText the raw attributes string (e.g. "id=\"1\" year=\"1965\"")
     */
    private void parseAttributes(XmlElement element, String attributesText) {
        while (attributesText.contains("=")) {
            int equalsIndex = attributesText.indexOf('=');
            String key = attributesText.substring(0, equalsIndex).trim();

            int firstQuoteIndex = attributesText.indexOf('"', equalsIndex);
            int secondQuoteIndex = attributesText.indexOf('"', firstQuoteIndex + 1);

            if (firstQuoteIndex == -1 || secondQuoteIndex == -1) break;

            String value = attributesText.substring(firstQuoteIndex + 1, secondQuoteIndex);

            element.addAttribute(key, value);

            attributesText = attributesText.substring(secondQuoteIndex + 1).trim();
        }
    }
}
