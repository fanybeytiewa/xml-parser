package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.models.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing attribute values (@).
 * Handles steps of the form "tagName(@attrName)" or "(@attrName)".
 * Example: section[1](@year), (@id)
 *
 * This is a terminal operation — returns {@code List<String>} instead of {@code List<XmlElement>}. */
public class AttributeAccessor {

    /**
     * Checks if the step contains an attribute access (@attr).
     * @param step the XPath step to check
     * @return true if the step accesses an attribute
     */
    public boolean isAttributeAccess(String step) {
        return step.contains("(@") && step.endsWith(")");
    }

    /**
     * Extracts the base part before the attribute accessor.
     * Example: "section[1](@year)" returns "section[1]".
     * @param step the full XPath step
     * @return the navigation part before (@...)
     */
    public String getBasePart(String step) {
        return step.substring(0, step.indexOf("(@")).trim();
    }

    /**
     * Extracts the attribute name from the step.
     * Example: "section[1](@year)" returns "year".
     * @param step the full XPath step
     * @return the attribute name
     */
    public String getAttributeName(String step) {
        return step.substring(step.indexOf("(@") + 2, step.length() - 1).trim();
    }

    /**
     * Extracts attribute values from a list of elements.
     * @param elements the elements to extract from
     * @param attrName the attribute name to look up
     * @return list of found attribute values
     */
    public List<String> extractValues(List<XmlElement> elements, String attrName) {
        List<String> values = new ArrayList<>();
        for (XmlElement el : elements) {
            String val = el.getAttributeByKey(attrName);
            if (val != null) {
                values.add(val);
            }
        }
        return values;
    }
}
