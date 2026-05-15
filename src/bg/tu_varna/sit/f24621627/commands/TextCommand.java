package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.models.XmlDocument;
import bg.tu_varna.sit.f24621627.models.XmlElement;

/** Command for printing the text content of an element. */
public class TextCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new TextCommand.
     * @param document the XML document to operate on
     */
    public TextCommand(XmlDocument document) {
        super("text", "<id>", "prints the text content of an element");
        this.document = document;
    }

    /** Prints the text content of an element. */
    @Override
    public void execute() {
        if (getArgs().length < 2) {
            System.out.println("Error: Invalid arguments. Usage: text <id>");
            return;
        }

        String targetId = getArgs()[1];
        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        String textContent = element.getTextContent();

        if (textContent != null && !textContent.trim().isEmpty()) {
            System.out.println(textContent);
        } else {
            System.out.println("Element '" + targetId + "' does not contain any text.");
        }
    }
}