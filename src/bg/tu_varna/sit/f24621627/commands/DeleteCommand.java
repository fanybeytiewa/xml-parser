package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

/** Command for deleting an attribute from an element by ID and key. */
public class DeleteCommand extends Command {
    private XmlDocument document;

    public DeleteCommand(XmlDocument document) {
        super("delete", "delete <id> <key>\tdeletes an attribute from an element");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length < 3) {
            System.out.println("Error: Invalid arguments. Usage: delete <id> <key>");
            return;
        }

        String targetId = getArgs()[1];
        String targetKey = getArgs()[2];
        XmlElement elementToDeleteFrom = document.getElementById(targetId);

        if (elementToDeleteFrom == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            if (elementToDeleteFrom.getAttributeByKey(targetKey) != null) {
                // If the key being deleted is "id", also remove it from the document registry
                if (targetKey.equals("id")) {
                    document.removeIdFromRegistry(targetId);
                }

                elementToDeleteFrom.removeAttribute(targetKey);
                System.out.println("Successfully deleted attribute '" + targetKey + "' from element '" + targetId + "'.");
            } else {
                System.out.println("Error: Attribute '" + targetKey + "' not found in element '" + targetId + "'.");
            }
        }
    }
}