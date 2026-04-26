package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

public class TextCommand extends Command {
    private XmlDocument document;

    public TextCommand(XmlDocument document) {
        super("text", "text <id>\t\t\tprints the text content of an element");
        this.document = document;
    }

    @Override
    public void execute() {
        if (args.length < 2) {
            System.out.println("Error: Invalid arguments. Usage: text <id>");
            return;
        }

        String targetId = args[1];
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