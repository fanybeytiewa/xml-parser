package bg.tu_varna.sit.f24621627;

import java.util.ArrayList;
import java.util.List;

public class XPathService {

    public List<String> evaluate(XmlElement startNode, String xpath) {
        String[] steps = xpath.split("/");
        List<XmlElement> currentElements = new ArrayList<>();
        currentElements.add(startNode);

        for (int i = 0; i < steps.length; i++) {
            String step = steps[i].trim();
            if (step.isEmpty()) continue;

            // ПРОВЕРКА: "section[1](@year)" или "section(@id)"
            if (step.contains("(@") && step.endsWith(")")) {
                // Вземаме всичко преди "(@" -> "section[1]"
                String basePart = step.substring(0, step.indexOf("(@")).trim();
                // Вземаме името на атрибута -> "year"
                String attrName = step.substring(step.indexOf("(@") + 2, step.length() - 1).trim();

                // Ако има базова част (напр. "section[1]"), първо навигираме до нея
                if (!basePart.isEmpty()) {
                    currentElements = navigateToNextLevel(currentElements, basePart);
                }

                // Връщаме атрибутите на тези конкретни елементи
                return extractAttributeValues(currentElements, attrName);
            }

            // Стандартна навигация за тагове, индекси [n] или филтри (key=val)
            currentElements = navigateToNextLevel(currentElements, step);
            if (currentElements.isEmpty()) {
                break;
            }
        }

        return finalizeResults(currentElements);
    }

    private List<XmlElement> navigateToNextLevel(List<XmlElement> parents, String step) {
        List<XmlElement> nextLevel = new ArrayList<>();
        for (XmlElement parent : parents) {
            nextLevel.addAll(getChildrenByStep(parent, step));
        }
        return nextLevel;
    }

    private List<XmlElement> getChildrenByStep(XmlElement parent, String step) {
        String tagName = step;
        String filterKey = null;
        String filterValue = null;
        Integer index = null;

        // СЛУЧАЙ: Индекс с квадратни скоби "address[0]"
        if (step.contains("[") && step.endsWith("]")) {
            tagName = step.substring(0, step.indexOf("[")).trim();
            String content = step.substring(step.indexOf("[") + 1, step.indexOf("]")).trim();
            try {
                index = Integer.parseInt(content);
            } catch (NumberFormatException e) { }
        }
        // СЛУЧАЙ: Филтър с кръгли скоби "section(category="Fantasy")"
        else if (step.contains("(") && step.endsWith(")")) {
            tagName = step.substring(0, step.indexOf("(")).trim();
            String content = step.substring(step.indexOf("(") + 1, step.indexOf(")")).trim();

            if (content.contains("=")) {
                String[] parts = content.split("=", 2);
                filterKey = parts[0].trim();
                filterValue = parts[1].trim().replaceAll("^[\"']|[\"']$", "");
            }
        }

        List<XmlElement> matches = findChildrenByTag(parent, tagName);
        if (filterKey != null) matches = filterByAttribute(matches, filterKey, filterValue);
        if (index != null) matches = selectByIndex(matches, index);

        return matches;
    }

    private List<XmlElement> findChildrenByTag(XmlElement parent, String tagName) {
        List<XmlElement> list = new ArrayList<>();
        if (parent.getChildren() != null) {
            for (XmlElement child : parent.getChildren()) {
                if (child.getTag().equals(tagName)) {
                    list.add(child);
                }
            }
        }
        return list;
    }

    private List<XmlElement> filterByAttribute(List<XmlElement> elements, String key, String value) {
        List<XmlElement> filtered = new ArrayList<>();
        for (XmlElement el : elements) {
            // 1. Първо проверяваме дали е атрибут (напр. category="Science Fiction")
            String attrVal = el.getAttributeByKey(key);
            if (value.equals(attrVal)) {
                filtered.add(el);
                continue;
            }

            // 2. Ако не е атрибут, проверяваме дали е вложен таг (напр. title="Dune")
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

    private List<XmlElement> selectByIndex(List<XmlElement> elements, int index) {
        List<XmlElement> result = new ArrayList<>();
        if (index >= 0 && index < elements.size()) {
            result.add(elements.get(index));
        }
        return result;
    }

    private List<String> extractAttributeValues(List<XmlElement> elements, String attrName) {
        List<String> values = new ArrayList<>();
        for (XmlElement el : elements) {
            String val = el.getAttributeByKey(attrName);
            if (val != null) {
                values.add(val);
            }
        }
        return values;
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