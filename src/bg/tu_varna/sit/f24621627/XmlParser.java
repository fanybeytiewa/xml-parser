package bg.tu_varna.sit.f24621627;

import java.util.Stack;

/**
 * Parses an XML string and builds a tree structure from {@link XmlElement} objects.
 * Handles the tree logic (nesting, parent-child relationships) and delegates
 * individual tag parsing to {@link SimpleTagParser} and {@link AttributeTagParser}.
 */
public class XmlParser implements ElementParser {

    /** Stack for tracking currently open elements during parsing. */
    private Stack<XmlElement> stack;

    /** The root element of the parsed XML tree. */
    private XmlElement root;

    /** Parser for tags without attributes. */
    private final SimpleTagParser simpleTagParser;

    /** Parser for tags with attributes. */
    private final AttributeTagParser attributeTagParser;

    /** Initializes the two tag parsers used during XML parsing. */
    public XmlParser() {
        this.simpleTagParser = new SimpleTagParser();
        this.attributeTagParser = new AttributeTagParser();
    }

    /**
     * Parses an XML string and returns the root element.
     * @param xmlContent the XML content as a string
     * @return the root element of the parsed tree
     * @throws XmlParseException if the XML structure is invalid
     */
    @Override
    public XmlElement parse(String xmlContent) throws XmlParseException {
        stack = new Stack<>();
        root = null;
        int currentIndex = 0;

        while (currentIndex < xmlContent.length()) {
            // Search for the next opening bracket
            int startBracket = xmlContent.indexOf('<', currentIndex);
            if (startBracket == -1) break;

            // Extract text content between tags
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

            if (tagContent.startsWith("/")) {
                handleClosingTag(tagContent);
            } else {
                handleOpeningTag(tagContent);
            }

            // Move the current index to the character after the closing bracket
            currentIndex = endBracket + 1;
        }

        return root;
    }

    /**
     * Handles a closing tag — validates that it matches the last opened tag.
     * @param tagContent the content between &lt; and &gt; (e.g. "/book")
     * @throws XmlParseException if tags are mismatched or no opening tag exists
     */
    private void handleClosingTag(String tagContent) throws XmlParseException {
        String closingTagName = tagContent.substring(1).trim();

        if (stack.isEmpty()) {
            throw new XmlParseException("Closing tag </" + closingTagName + "> has no matching opening tag.");
        }

        XmlElement lastOpened = stack.peek();
        if (!lastOpened.getTag().equals(closingTagName)) {
            throw new XmlParseException("Mismatched tags: expected </" +
                    lastOpened.getTag() + "> but found </" + closingTagName +
                    ">. Last opened element: " + lastOpened);
        }

        stack.pop();
    }

    /**
     * Handles an opening tag — delegates to TagParser for element creation,
     * then manages the tree structure (parent-child relationships).
     * @param tagContent the content between &lt; and &gt; (e.g. "book id=\"1\"")
     */
    private void handleOpeningTag(String tagContent) throws XmlParseException {
        boolean isSelfClosing = false;

        if (tagContent.endsWith("/")) {
            isSelfClosing = true;
            tagContent = tagContent.substring(0, tagContent.length() - 1).trim();
        }

        // Choose the appropriate parser based on content
        ElementParser parser = tagContent.contains(" ") ? attributeTagParser : simpleTagParser;
        XmlElement newElement = parser.parse(tagContent);

        if (root == null) {
            root = newElement;
        } else if (!stack.isEmpty()) {
            stack.peek().addChild(newElement);
        }

        if (!isSelfClosing) {
            stack.push(newElement);
        }
    }
}