package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing attribute values (@).
 * Handles steps of the form "tagName(@attrName)" or "(@attrName)".
 * Example: section[1](@year), (@id)
 *
 * This is a terminal operation — returns List<String> instead of List<XmlElement>.
 */
public class AttributeAccessor {

    /**
     * Checks if the step is an attribute access.
     */
    public boolean isAttributeAccess(String step) {
        return step.contains("(@") && step.endsWith(")");
    }

    /**
     * Extracts the base part before (@...).
     * Example: "section[1](@year)" → "section[1]"
     */
    public String getBasePart(String step) {
        return step.substring(0, step.indexOf("(@")).trim();
    }

    /**
     * Extracts the attribute name.
     * Example: "section[1](@year)" → "year"
     */
    public String getAttributeName(String step) {
        return step.substring(step.indexOf("(@") + 2, step.length() - 1).trim();
    }

    /**
     * Extracts attribute values from a list of elements.
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
