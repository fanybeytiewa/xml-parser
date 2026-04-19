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
                // Търсим децата, които съвпадат по име с текущата стъпка
                nextLevel.addAll(getChildrenByTagName(el, step));
            }
            currentElements = nextLevel;

            if (currentElements.isEmpty()) break;
        }

        // Извличаме "стойностите" от финалните елементи
        List<String> results = new ArrayList<>();
        for (XmlElement el : currentElements) {
            // 1. Първо проверяваме за текст (textContent)
            if (el.getTextContent() != null && !el.getTextContent().trim().isEmpty()) {
                results.add(el.getTextContent().trim());
            }
            // 2. Ако няма текст, събираме стойностите на всички негови атрибути
            else if (!el.getAttributes().isEmpty()) {
                // Събираме всички стойности на атрибути в един низ (напр. "1965 BGN")
                String attrValues = String.join(" ", el.getAttributes().values());
                results.add(attrValues);
            }
        }
        return results;
    }

    private List<XmlElement> getChildrenByTagName(XmlElement parent, String tagName) {
        List<XmlElement> matches = new ArrayList<>();
        if (parent.getChildren() != null) {
            for (XmlElement child : parent.getChildren()) {
                if (child.getTag().equals(tagName)) {
                    matches.add(child);
                }
            }
        }
        return matches;
    }
}
