package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator for filtering by attribute.
 * Handles steps of the form "tagName(key=val)".
 * Example: section(category="Fantasy"), book(title="Dune")
 */
public class AttributeFilterOperator extends XPathOperator {

    /**
     * Returns true for steps with parentheses but not attribute access.
     * @param step the XPath step
     * @return true if the step contains a filter expression
     */
    @Override
    public boolean canHandle(String step) {
        // Handles parentheses, but NOT attribute access (@)
        return step.contains("(") && step.endsWith(")") && !step.contains("(@");
    }

    /**
     * Navigates by tag name and filters by attribute key-value pair.
     * @param parents the parent elements
     * @param step the step with filter (e.g. "book(year=1965)")
     * @return list of elements matching the filter
     */
    @Override
    public List<XmlElement> apply(List<XmlElement> parents, String step) {
        String tagName = step.substring(0, step.indexOf("(")).trim();
        String content = step.substring(step.indexOf("(") + 1, step.indexOf(")")).trim();

        String filterKey = null;
        String filterValue = null;

        if (content.contains("=")) {
            String[] parts = content.split("=", 2);
            filterKey = parts[0].trim();
            filterValue = parts[1].trim().replaceAll("^[\"']|[\"']$", "");
        }

        // First navigate by tag, then filter
        List<XmlElement> matches = findChildrenByTag(parents, tagName);

        if (filterKey != null) {
            matches = filterByAttribute(matches, filterKey, filterValue);
        }

        return matches;
    }

    /**
     * Filters elements by an attribute key-value match or nested tag text match.
     * @param elements the elements to filter
     * @param key the attribute key or child tag name
     * @param value the expected value
     * @return list of elements matching the filter
     */
    private List<XmlElement> filterByAttribute(List<XmlElement> elements, String key, String value) {
        List<XmlElement> filtered = new ArrayList<>();
        for (XmlElement el : elements) {
            // 1. First check if it is an attribute (e.g. category="Science Fiction")
            String attrVal = el.getAttributeByKey(key);
            if (value.equals(attrVal)) {
                filtered.add(el);
                continue;
            }

            // 2. If not an attribute, check if it is a nested tag (e.g. title="Dune")
            if (el.getChildren() != null) {
                for (XmlElement child : el.getChildren()) {
                    if (child.getTag().equalsIgnoreCase(key) && value.equals(child.getTextContent())) {
                        filtered.add(el);
                        break;
                    }
                }
            }
        }
        return filtered;
    }
}
