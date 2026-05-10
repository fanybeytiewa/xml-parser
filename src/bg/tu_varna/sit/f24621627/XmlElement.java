package bg.tu_varna.sit.f24621627;

import java.util.*;

public class XmlElement {
    private String tag;
    private String textContent;
    private Map<String, String> attributes;
    private List<XmlElement> children;

    public XmlElement(String tag) {
        this.tag = tag;
        this.textContent = "";
        this.attributes = new LinkedHashMap<>(); // LinkedHashMap to preserve attribute order
        this.children = new ArrayList<>();
    }

    public String getTag() {
        return tag;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public String getAttributeByKey (String key) {
        return this.attributes.get(key);
    }

    public String getId() {
        return this.attributes.get("id");
    }

    public void setId(String id) {
        this.attributes.put("id", id);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void addChild(XmlElement child) {
        this.children.add(child);
    }

    public List<XmlElement> getChildren() {
        return this.children;
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public String toXml(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        // 4 интервала за всяко ниво на навътре (indentation)
        String spaces = "";
        for (int i = 0; i < indentLevel; i++) {
            spaces += "    ";
        }

        sb.append(spaces).append("<").append(tag);

        // Ескейпваме стойностите на атрибутите
        for (String key : attributes.keySet()) {
            sb.append(" ")
                    .append(key)
                    .append("=\"")
                    .append(escapeXml(attributes.get(key)))
                    .append("\"");
        }
        sb.append(">");

        // Ескейпваме текстовото съдържание
        if (textContent != null && !textContent.isEmpty()) {
            sb.append(escapeXml(textContent));
        } else if (!children.isEmpty()) {
            sb.append("\n");
            for (XmlElement child : children) {
                sb.append(child.toXml(indentLevel + 1));
            }
            sb.append(spaces);
        }

        sb.append("</").append(tag).append(">\n");

        return sb.toString();
    }
}
