package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.List;

/**
 * Operator for tag navigation (/).
 * Handles simple tag names without index or filter.
 * Example: "section", "book", "title"
 */
public class TagNavigationOperator extends XPathOperator {

    @Override
    public boolean canHandle(String step) {
        // Can handle steps without square or round brackets
        return !step.contains("[") && !step.contains("(");
    }

    @Override
    public List<XmlElement> apply(List<XmlElement> parents, String step) {
        return findChildrenByTag(parents, step.trim());
    }
}
