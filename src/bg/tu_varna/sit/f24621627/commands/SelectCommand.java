package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

public class SelectCommand extends Command {
    private XmlDocument document;

    public SelectCommand(XmlDocument document) {
        super("select", "select <id> <key>\tselects an attribute from an element");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length < 3) {
            System.out.println("Error: Invalid arguments. Usage: select <id> <key>");
            return;
        }

        String targetId = getArgs()[1];
        String targetKey = getArgs()[2];
        XmlElement elementToSelect = document.getElementById(targetId);

        if (elementToSelect == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            String attributeValue = elementToSelect.getAttributeByKey(targetKey);
            if (attributeValue != null) {
                System.out.println(attributeValue);
            } else {
                System.out.println("Error: Attribute '" + targetKey + "' not found in element '" + targetId + "'.");
            }
        }
    }
}