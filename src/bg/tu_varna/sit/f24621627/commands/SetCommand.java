package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

/** Command for setting an attribute value. */
public class SetCommand extends Command {
    private XmlDocument document;

    public SetCommand(XmlDocument document) {
        super("set", "set <id> <key> <val>\tsets attribute value");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length < 4) {
            System.out.println("Error: Invalid arguments. Usage: set <id> <key> <value>");
            return;
        }

        String targetId = getArgs()[1];
        String targetKey = getArgs()[2];

        if (targetKey.trim().isEmpty()) {
            System.out.println("Error: Attribute key cannot be empty.");
            return;
        }

        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 3; i < getArgs().length; i++) {
            valueBuilder.append(getArgs()[i]);
            if (i < getArgs().length - 1) {
                valueBuilder.append(" ");
            }
        }

        String targetValue = valueBuilder.toString().replace("\"", "");
        XmlElement elementToSet = document.getElementById(targetId);

        if (elementToSet == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            // If we are changing the ID itself
            if (targetKey.equals("id")) {
                // Check if the new ID is already taken
                if (document.getElementById(targetValue) != null) {
                    System.out.println("Error: ID '" + targetValue + "' is already taken.");
                    return;
                }
                document.updateIdInRegistry(targetId, targetValue, elementToSet);
            }

            elementToSet.addAttribute(targetKey, targetValue);
            System.out.println("Successfully set attribute '" + targetKey + "' to '" + targetValue + "' for element '" + targetId + "'.");
        }
    }
}