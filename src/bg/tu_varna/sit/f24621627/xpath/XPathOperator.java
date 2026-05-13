package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for an XPath operator.
 * Each operator handles a specific type of step in an XPath expression.
 * Contains a shared method for navigation by tag name.
 */
public abstract class XPathOperator {

    /**
     * Checks whether this operator can handle the given step.
     * @param step the XPath step expression
     * @return true if this operator can process the step
     */
    public abstract boolean canHandle(String step);

    /**
     * Applies the operator to a list of parent elements.
     * @param parents the parent elements to process
     * @param step the XPath step expression
     * @return list of matching child elements
     */
    public abstract List<XmlElement> apply(List<XmlElement> parents, String step);

    /**
     * Shared method — finds all children with a given tag.
     * Subclasses use it for navigation before their specific processing.
     */
    protected List<XmlElement> findChildrenByTag(List<XmlElement> parents, String tagName) {
        List<XmlElement> result = new ArrayList<>();
        for (XmlElement parent : parents) {
            if (parent.getChildren() != null) {
                for (XmlElement child : parent.getChildren()) {
                    if (child.getTag().equals(tagName)) {
                        result.add(child);
                    }
                }
            }
        }
        return result;
    }
}
