package bg.tu_varna.sit.f24621627;

import java.util.ArrayList;
import java.util.List;

public class XPathService {

    public List<String> evaluate(XmlElement startNode, String xpath) {
        String[] steps = xpath.split("/");
        List<XmlElement> currentElements = new ArrayList<>();
        currentElements.add(startNode);

        for (String step : steps) {
            if (step.isEmpty()) continue;

            List<XmlElement> nextLevel = new ArrayList<>();
            for (XmlElement el : currentElements) {
                nextLevel.addAll(getChildrenByStep(el, step));
            }
            currentElements = nextLevel;

            if (currentElements.isEmpty()) break;
        }

        List<String> results = new ArrayList<>();
        for (XmlElement el : currentElements) {
            if (el.getTextContent() != null && !el.getTextContent().trim().isEmpty()) {
                results.add(el.getTextContent().trim());
            } else if (!el.getAttributes().isEmpty()) {
                results.add(String.join(" ", el.getAttributes().values()));
            }
        }
        return results;
    }

    private List<XmlElement> getChildrenByStep(XmlElement parent, String step) {
        String tagName = step;
        Integer index = null;
        String attrKey = null;
        String attrValue = null;

        // Проверка за скоби [...]
        if (step.contains("[") && step.endsWith("]")) {
            tagName = step.substring(0, step.indexOf("["));
            String content = step.substring(step.indexOf("[") + 1, step.length() - 1);

            if (content.contains("=")) {
                String[] parts = content.split("=");
                attrKey = parts[0].trim();
                // Премахваме кавичките около стойността, ако ги има
                attrValue = parts[1].trim().replace("\"", "").replace("'", "");
            } else {
                try {
                    index = Integer.parseInt(content);
                } catch (NumberFormatException e) {
                    index = null;
                }
            }
        }

        List<XmlElement> candidates = new ArrayList<>();
        if (parent.getChildren() != null) {
            for (XmlElement child : parent.getChildren()) {
                if (child.getTag().equals(tagName)) {
                    // Филтрираме по атрибут, ако има условие
                    if (attrKey != null) {
                        String actualValue = child.getAttributeByKey(attrKey);
                        if (actualValue != null && actualValue.equals(attrValue)) {
                            candidates.add(child);
                        }
                    } else {
                        candidates.add(child);
                    }
                }
            }
        }

        // Връщаме или конкретен индекс, или всички филтрирани кандидати
        if (index != null) {
            List<XmlElement> singleMatch = new ArrayList<>();
            if (index >= 0 && index < candidates.size()) {
                singleMatch.add(candidates.get(index));
            }
            return singleMatch;
        }

        return candidates;
    }
}