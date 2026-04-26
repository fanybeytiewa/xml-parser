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
                // извикваме новия метод, който ще се справя с тагове и индекси като [0]
                nextLevel.addAll(getChildrenByStep(el, step));
            }
            currentElements = nextLevel;

            if (currentElements.isEmpty()) break;
        }

        // Извличаме стойностите от финалните елементи
        List<String> results = new ArrayList<>();
        for (XmlElement el : currentElements) {
            if (el.getTextContent() != null && !el.getTextContent().trim().isEmpty()) {
                results.add(el.getTextContent().trim());
            } else if (!el.getAttributes().isEmpty()) {
                // Ако няма текст, връщаме стойностите на атрибутите
                String attrValues = String.join(" ", el.getAttributes().values());
                results.add(attrValues);
            }
        }
        return results;
    }


    private List<XmlElement> getChildrenByStep(XmlElement parent, String step) {
        String tagName = step;
        Integer index = null;

        // ПРОВЕРКА ЗА ИНДЕКС
        if (step.contains("[") && step.endsWith("]")) {
            tagName = step.substring(0, step.indexOf("["));
            String indexStr = step.substring(step.indexOf("[") + 1, step.length() - 1);
            try {
                index = Integer.parseInt(indexStr);
            } catch (NumberFormatException e) {
                index = null;
            }
        }

        // Събираме всички деца, които съвпадат по име
        List<XmlElement> sameTagChildren = new ArrayList<>();
        if (parent.getChildren() != null) {
            for (XmlElement child : parent.getChildren()) {
                if (child.getTag().equals(tagName)) {
                    sameTagChildren.add(child);
                }
            }
        }

        // Ако има валиден индекс, създаваме нов малък списък само с 1 елемент и го връщаме
        if (index != null) {
            List<XmlElement> singleMatch = new ArrayList<>();
            if (index >= 0 && index < sameTagChildren.size()) {
                singleMatch.add(sameTagChildren.get(index));
            }
            return singleMatch;
        }

        // Ако НЯМА индекс, направо връщаме списъка, който вече напълнихме
        return sameTagChildren;
    }
}