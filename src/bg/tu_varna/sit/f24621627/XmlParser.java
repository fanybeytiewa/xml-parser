package bg.tu_varna.sit.f24621627;

import java.util.Stack;

public class XmlParser {

    public XmlElement parse(String xmlContent) {
        Stack<XmlElement> stack = new Stack<>();
        XmlElement root = null;
        int currentIndex = 0;

        while (currentIndex < xmlContent.length()) {
            // search for the next opening bracket
            int startBracket = xmlContent.indexOf('<', currentIndex);
            if (startBracket == -1) break;

            // search for text content between the current index and the next opening bracket
            if (startBracket > currentIndex && !stack.isEmpty()) {
                String text = xmlContent.substring(currentIndex, startBracket).trim();
                if (!text.isEmpty()) {
                    stack.peek().setTextContent(text);
                }
            }

            // search for the closing bracket of the current tag
            int endBracket = xmlContent.indexOf('>', startBracket);
            if (endBracket == -1) break;

            // get the content of the tag (between < and >)
            String tagContent = xmlContent.substring(startBracket + 1, endBracket).trim();

            // check if it's a closing tag (starts with "/")
            if (tagContent.startsWith("/")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                boolean isSelfClosing = false;

                if (tagContent.endsWith("/")) {
                    isSelfClosing = true;
                    // trim the closing "/" from the tag content
                    tagContent = tagContent.substring(0, tagContent.length() - 1).trim();
                }

                XmlElement newElement = createNodeFromTag(tagContent);

                if (root == null) {
                    root = newElement;
                } else if (!stack.isEmpty()) {
                    stack.peek().addChild(newElement);
                }

                // put the new element on the stack if it's not self-closing
                if (!isSelfClosing) {
                    stack.push(newElement);
                }
            }

            // move the current index to the character after the closing bracket
            currentIndex = endBracket + 1;
        }

        return root;
    }

    private XmlElement createNodeFromTag(String innerText) {
        int firstSpaceIndex = innerText.indexOf(' ');
        String tagName;
        String attributesText;

        // split the tag name and the attributes text
        if (firstSpaceIndex == -1) {
            tagName = innerText;
            attributesText = "";
        } else {
            tagName = innerText.substring(0, firstSpaceIndex);
            attributesText = innerText.substring(firstSpaceIndex + 1).trim();
        }

        XmlElement element = new XmlElement(tagName);

        // get attributes in the format key="value"
        while (attributesText.contains("=")) {
            int equalsIndex = attributesText.indexOf('=');
            String key = attributesText.substring(0, equalsIndex).trim();

            int firstQuoteIndex = attributesText.indexOf('"', equalsIndex);
            int secondQuoteIndex = attributesText.indexOf('"', firstQuoteIndex + 1);

            if (firstQuoteIndex == -1 || secondQuoteIndex == -1) break;

            String value = attributesText.substring(firstQuoteIndex + 1, secondQuoteIndex);

            element.addAttribute(key, value);

            attributesText = attributesText.substring(secondQuoteIndex + 1).trim();
        }

        return element;
    }
}
