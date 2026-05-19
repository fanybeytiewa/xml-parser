package bg.tu_varna.sit.f24621627.models;

import java.util.*;
import java.util.Collections;

/**
 * Represents a single XML element.
 * Contains a tag, text content, attributes and nested child elements.
 */
public class XmlElement {
    /** The tag name of the element (e.g. "book", "title"). */
    private String tag;

    /** The text content between the opening and closing tags. */
    private String textContent;

    /** Map of attribute key-value pairs. Uses LinkedHashMap to preserve order. */
    private Map<String, String> attributes;

    /** List of nested child elements. */
    private List<XmlElement> children;

    /** Parent element (null if this is the root element). */
    private XmlElement parent;

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
        this.parent = null;
    }

    /**
     * Returns the tag name.
     * @return the tag name
     */
    public String getTag() {
        return tag;
    }

    /**
     * Returns the text content of the element.
     * @return the text content of the element
     */
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

    /**
     * Returns the value of the "id" attribute.
     * @return the value of the "id" attribute or null
     */
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
     * Adds a child element and automatically sets its parent to this element.
     * @param child the element to add
     */
    public void addChild(XmlElement child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Returns the parent element.
     * @return the parent element, or null if root
     */
    public XmlElement getParent() {
        return parent;
    }

    /**
     * Sets the parent element.
     * @param parent the parent element
     */
    public void setParent(XmlElement parent) {
        this.parent = parent;
    }

    /**
     * Extracts the namespace prefix from the tag name.
     * @return the prefix, or an empty string if there is no prefix
     */
    public String getNamespacePrefix() {
        if (tag != null && tag.contains(":")) {
            return tag.substring(0, tag.indexOf(":"));
        }
        return "";
    }

    /**
     * Extracts the local name from the tag name (without the namespace prefix).
     * @return the local name
     */
    public String getLocalName() {
        if (tag != null && tag.contains(":")) {
            return tag.substring(tag.indexOf(":") + 1);
        }
        return tag;
    }

    /**
     * Resolves the namespace URI for a given prefix.
     * Searches attributes of this element, then recursively asks the parent.
     * @param prefix the namespace prefix (or empty string for default namespace)
     * @return the namespace URI, or null if not found
     */
    public String getNamespaceURI(String prefix) {
        String attrKey = (prefix == null || prefix.isEmpty()) ? "xmlns" : "xmlns:" + prefix;
        String uri = getAttributeByKey(attrKey);
        
        if (uri != null) {
            return uri;
        } else if (parent != null) {
            return parent.getNamespaceURI(prefix);
        }
        return null;
    }

    /**
     * Returns the namespace URI associated with this element's tag.
     * @return the namespace URI, or null if not defined
     */
    public String getNamespaceURI() {
        return getNamespaceURI(getNamespacePrefix());
    }

    /**
     * Returns an unmodifiable list of child elements.
     * @return list of child elements
     */
    public List<XmlElement> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * Returns a human-readable summary of this element.
     * Useful for debugging and logging.
     * Example: XmlElement{tag='book', id='1', attributes=2, children=3}
     * @return string representation of the element
     */
    @Override
    public String toString() {
        return "XmlElement { tag = '" + tag + "'" +
                ", id = '" + getId() + "'" +
                ", attributes count = " + attributes.size() +
                ", children count = " + children.size() +
                " }";
    }
}

