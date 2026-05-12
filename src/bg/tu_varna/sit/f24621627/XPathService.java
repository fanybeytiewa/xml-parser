package bg.tu_varna.sit.f24621627;

import bg.tu_varna.sit.f24621627.xpath.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for evaluating XPath queries.
 * Coordinates the work of individual operators (Strategy Pattern).
 * Each operator handles a different type of step.
 */
public class XPathService {

    private final List<XPathOperator> operators;

    public XPathService() {
        operators = new ArrayList<>();
        // Order matters — more specific operators are checked first
        operators.add(new IndexOperator());
        operators.add(new AttributeFilterOperator());
        operators.add(new TagNavigationOperator()); // fallback — handles simple tag names
    }

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

    private List<String> finalizeResults(List<XmlElement> elements) {
        List<String> results = new ArrayList<>();
        for (XmlElement el : elements) {
            String text = el.getTextContent();
            if (text != null && !text.trim().isEmpty()) {
                results.add(text.trim());
            } else if (!el.getAttributes().isEmpty()) {
                results.add(String.join(" ", el.getAttributes().values()));
            }
        }
        return results;
    }
}