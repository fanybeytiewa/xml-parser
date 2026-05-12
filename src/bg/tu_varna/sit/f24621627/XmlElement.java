package bg.tu_varna.sit.f24621627;

import java.util.*;
import java.util.Collections;

/**
 * Represents a single XML element.
 * Contains a tag, text content, attributes and nested child elements.
 */
public class XmlElement {
    private String tag;
    private String textContent;
    private Map<String, String> attributes;
    private List<XmlElement> children;

    /**
     * Creates a new XML element with the given tag name.
     * @param tag the tag name (cannot be null or empty)
     * @throws IllegalArgumentException if tag is null or empty
     */
    public XmlElement(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty.");
        }
        this.tag = tag;
        this.textContent = "";
        this.attributes = new LinkedHashMap<>();
        this.children = new ArrayList<>();
    }

    /** @return the tag name */
    public String getTag() {
        return tag;
    }

    /** @return the text content of the element */
    public String getTextContent() {
        return textContent;
    }

    /**
     * Sets the text content of the element.
     * @param textContent the text to set
     */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /**
     * Adds an attribute to the element.
     * @param key the attribute key (cannot be null or empty)
     * @param value the attribute value (cannot be null)
     * @throws IllegalArgumentException if key is null/empty or value is null
     */
    public void addAttribute(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute key cannot be null or empty.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Attribute value cannot be null.");
        }
        this.attributes.put(key, value);
    }

    /**
     * Removes an attribute by key.
     * @param key the attribute key to remove
     */
    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    /**
     * Returns the value of an attribute by key.
     * @param key the attribute key
     * @return the value or null if not found
     */
    public String getAttributeByKey(String key) {
        return this.attributes.get(key);
    }

    /** @return the value of the "id" attribute or null */
    public String getId() {
        return this.attributes.get("id");
    }

    /**
     * Sets the unique identifier of the element.
     * @param id the new identifier
     */
    public void setId(String id) {
        this.attributes.put("id", id);
    }

    /**
     * Returns an unmodifiable map of the element's attributes.
     * @return key-value map of attributes
     */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    /**
     * Adds a child element.
     * @param child the element to add
     */
    public void addChild(XmlElement child) {
        this.children.add(child);
    }

    /**
     * Returns an unmodifiable list of child elements.
     * @return list of child elements
     */
    public List<XmlElement> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Serializes the element and its children to a formatted XML string.
     * @param indentLevel the indentation level (0 for root)
     * @return formatted XML string
     */
    public String toXml(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        // 4 spaces per indentation level
        String spaces = "";
        for (int i = 0; i < indentLevel; i++) {
            spaces += "    ";
        }

        sb.append(spaces).append("<").append(tag);

        // Escape attribute values
        for (String key : attributes.keySet()) {
            sb.append(" ")
                    .append(key)
                    .append("=\"")
                    .append(escapeXml(attributes.get(key)))
                    .append("\"");
        }
        sb.append(">");

        // Escape text content
        if (textContent != null && !textContent.isEmpty()) {
            sb.append(escapeXml(textContent));
        } else if (!children.isEmpty()) {
            sb.append("\n");
            for (XmlElement child : children) {
                sb.append(child.toXml(indentLevel + 1));
            }
            sb.append(spaces);
        }

        sb.append("</").append(tag).append(">\n");

        return sb.toString();
    }
}
