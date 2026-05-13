package bg.tu_varna.sit.f24621627;

/**
 * Serializes an {@link XmlElement} tree into a formatted XML string.
 * Handles indentation, attribute escaping, and recursive child serialization.
 * Separates serialization logic from the data model.
 */
public class XmlSerializer {

    /**
     * Converts an XmlElement and its children to a formatted XML string.
     * @param element the element to serialize
     * @param indentLevel the indentation level (0 for root)
     * @return formatted XML string
     */
    public String serialize(XmlElement element, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String spaces = buildIndent(indentLevel);

        sb.append(spaces).append("<").append(element.getTag());
        appendAttributes(sb, element);
        sb.append(">");

        appendContent(sb, element, indentLevel, spaces);

        sb.append("</").append(element.getTag()).append(">\n");

        return sb.toString();
    }

    /**
     * Builds the indentation string for a given level.
     * @param level the indentation level
     * @return a string of spaces (4 per level)
     */
    private String buildIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }

    /**
     * Appends all attributes to the string builder in key="value" format.
     * @param sb the string builder to append to
     * @param element the element whose attributes to serialize
     */
    private void appendAttributes(StringBuilder sb, XmlElement element) {
        for (String key : element.getAttributes().keySet()) {
            sb.append(" ")
                    .append(key)
                    .append("=\"")
                    .append(escapeXml(element.getAttributes().get(key)))
                    .append("\"");
        }
    }

    /**
     * Appends text content or recursively serialized children.
     * @param sb the string builder to append to
     * @param element the element to serialize content for
     * @param indentLevel the current indentation level
     * @param spaces the indentation string for the current level
     */
    private void appendContent(StringBuilder sb, XmlElement element, int indentLevel, String spaces) {
        String text = element.getTextContent();
        if (text != null && !text.isEmpty()) {
            sb.append(escapeXml(text));
        } else if (!element.getChildren().isEmpty()) {
            sb.append("\n");
            for (XmlElement child : element.getChildren()) {
                sb.append(serialize(child, indentLevel + 1));
            }
            sb.append(spaces);
        }
    }

    /**
     * Escapes special XML characters in a string.
     * @param input the string to escape
     * @return the escaped string
     */
    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
