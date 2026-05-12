package bg.tu_varna.sit.f24621627;

import java.util.Stack;

/**
 * Parses an XML string and builds a tree structure from {@link XmlElement} objects.
 * Does not use external libraries — the implementation is entirely manual.
 */
public class XmlParser {

    /**
     * Parses an XML string and returns the root element.
     * @param xmlContent the XML content as a string
     * @return the root element of the parsed tree
     * @throws XmlParseException if the XML structure is invalid
     */
    public XmlElement parse(String xmlContent) throws XmlParseException {
        Stack<XmlElement> stack = new Stack<>();
        XmlElement root = null;
        int currentIndex = 0;

        while (currentIndex < xmlContent.length()) {
            // Search for the next opening bracket
            int startBracket = xmlContent.indexOf('<', currentIndex);
            if (startBracket == -1) break;

            // Search for text content between the current index and the next opening bracket
            if (startBracket > currentIndex && !stack.isEmpty()) {
                String text = xmlContent.substring(currentIndex, startBracket).trim();
                if (!text.isEmpty()) {
                    stack.peek().setTextContent(text);
                }
            }

            // Search for the closing bracket of the current tag
            int endBracket = xmlContent.indexOf('>', startBracket);
            if (endBracket == -1) break;

            // Get the content of the tag (between < and >)
            String tagContent = xmlContent.substring(startBracket + 1, endBracket).trim();

            // Check if it's a closing tag (starts with "/")
            if (tagContent.startsWith("/")) {
                String closingTagName = tagContent.substring(1).trim();

                // Validation of closing tags
                if (stack.isEmpty()) {
                    throw new XmlParseException("Closing tag </" + closingTagName + "> has no matching opening tag.");
                }

                XmlElement lastOpened = stack.peek();
                if (!lastOpened.getTag().equals(closingTagName)) {
                    throw new XmlParseException("Mismatched tags. Expected </" +
                            lastOpened.getTag() + "> but found </" + closingTagName + ">");
                }

                stack.pop();
            } else {
                boolean isSelfClosing = false;

                if (tagContent.endsWith("/")) {
                    isSelfClosing = true;
                    // Trim the closing "/" from the tag content
                    tagContent = tagContent.substring(0, tagContent.length() - 1).trim();
                }

                XmlElement newElement = createNodeFromTag(tagContent);

                if (root == null) {
                    root = newElement;
                } else if (!stack.isEmpty()) {
                    stack.peek().addChild(newElement);
                }

                // Put the new element on the stack if it's not self-closing
                if (!isSelfClosing) {
                    stack.push(newElement);
                }
            }

            // Move the current index to the character after the closing bracket
            currentIndex = endBracket + 1;
        }

        return root;
    }

    private XmlElement createNodeFromTag(String innerText) {
        int firstSpaceIndex = innerText.indexOf(' ');
        String tagName;
        String attributesText;

        // Split the tag name and the attributes text
        if (firstSpaceIndex == -1) {
            tagName = innerText;
            attributesText = "";
        } else {
            tagName = innerText.substring(0, firstSpaceIndex);
            attributesText = innerText.substring(firstSpaceIndex + 1).trim();
        }

        XmlElement element = new XmlElement(tagName);

        // Get attributes in the format key="value"
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