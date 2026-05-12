package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator for selection by index ([]).
 * Handles steps of the form "tagName[n]".
 * Example: "address[0]", "section[1]"
 */
public class IndexOperator extends XPathOperator {

    @Override
    public boolean canHandle(String step) {
        return step.contains("[") && step.endsWith("]");
    }

    @Override
    public List<XmlElement> apply(List<XmlElement> parents, String step) {
        String tagName = step.substring(0, step.indexOf("[")).trim();
        String content = step.substring(step.indexOf("[") + 1, step.indexOf("]")).trim();

        int index;
        try {
            index = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }

        // First navigate by tag, then select by index
        List<XmlElement> matches = findChildrenByTag(parents, tagName);
        return selectByIndex(matches, index);
    }

    private List<XmlElement> selectByIndex(List<XmlElement> elements, int index) {
        List<XmlElement> result = new ArrayList<>();
        if (index >= 0 && index < elements.size()) {
            result.add(elements.get(index));
        }
        return result;
    }
}
