package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.List;

/**
 * Operator for tag navigation (/).
 * Handles simple tag names without index or filter.
 * Example: "section", "book", "title"
 */
public class TagNavigationOperator extends XPathOperator {

    /**
     * Returns true for simple tag names without brackets.
     * @param step the XPath step
     * @return true if no brackets are present
     */
    @Override
    public boolean canHandle(String step) {
        // Can handle steps without square or round brackets
        return !step.contains("[") && !step.contains("(");
    }

    /**
     * Finds all children matching the tag name.
     * @param parents the parent elements
     * @param step the tag name to match
     * @return list of matching child elements
     */
    @Override
    public List<XmlElement> apply(List<XmlElement> parents, String step) {
        return findChildrenByTag(parents, step.trim());
    }
}
