package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.models.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for evaluating XPath queries.
 * Coordinates the work of individual operators (Strategy Pattern).
 * Each operator handles a different type of step.
 */
public class XPathService {

    /** List of XPath operators, ordered from most specific to fallback. */
    private final List<XPathOperator> operators;

    /** Initializes the available XPath operators in priority order. */
    public XPathService() {
        operators = new ArrayList<>();
        // Order matters — more specific operators are checked first
        operators.add(new IndexOperator());
        operators.add(new AttributeFilterOperator());
        operators.add(new TagNavigationOperator()); // fallback — handles simple tag names
    }

    /**
     * Evaluates an XPath expression starting from the given element.
     * @param startNode the element to start evaluation from
     * @param xpath the XPath expression (e.g. "section/book/title")
     * @return list of matching text values or attribute values
     */
    public List<String> evaluate(XmlElement startNode, String xpath) {
        String[] steps = xpath.split("/");
        List<XmlElement> currentElements = new ArrayList<>();
        currentElements.add(startNode);

        AttributeAccessor accessor = new AttributeAccessor();

        for (String step : steps) {
            step = step.trim();
            if (step.isEmpty()) continue;

            // Check for attribute access (@attr) — terminal operation
            if (accessor.isAttributeAccess(step)) {
                String basePart = accessor.getBasePart(step);
                String attrName = accessor.getAttributeName(step);

                if (!basePart.isEmpty()) {
                    currentElements = applyOperator(currentElements, basePart);
                }

                return accessor.extractValues(currentElements, attrName);
            }

            // Standard navigation — find the matching operator
            currentElements = applyOperator(currentElements, step);
            if (currentElements.isEmpty()) {
                break;
            }
        }

        return finalizeResults(currentElements);
    }

    /**
     * Finds the matching operator for the given step and applies it.
     * Uses polymorphism — each operator decides if it can handle the step.
     */
    private List<XmlElement> applyOperator(List<XmlElement> elements, String step) {
        for (XPathOperator operator : operators) {
            if (operator.canHandle(step)) {
                return operator.apply(elements, step);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Converts the final list of elements to string results.
     * Returns text content if available, otherwise attribute values.
     * @param elements the elements to convert
     * @return list of string representations
     */
    private List<String> finalizeResults(List<XmlElement> elements) {
        List<String> results = new ArrayList<>();
        for (XmlElement el : elements) {
            String text = el.getTextContent();
            if (text != null && !text.trim().isEmpty()) {
                results.add(text.trim());
            } else {
                // Return the beautiful object-oriented representation
                results.add(el.toString());
            }
        }
        return results;
    }
}