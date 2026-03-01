package bg.tu_varna.sit.f24621627;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElement {
    private String tag;
    private String textContent;
    private Map<String, String> attributes;
    private List<XmlElement> children;

    public XmlElement(String tag, String textContent) {
        this.tag = tag;
        this.textContent = textContent;
        this.attributes = new HashMap<>();
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

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void addChild(XmlElement child) {
        this.children.add(child);
    }

    public List<XmlElement> getChildren() {
        return this.children;
    }
}
