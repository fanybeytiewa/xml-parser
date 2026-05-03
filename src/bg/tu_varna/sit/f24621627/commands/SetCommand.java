package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

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
            elementToSet.addAttribute(targetKey, targetValue);
            System.out.println("Successfully set attribute '" + targetKey + "' to '" + targetValue + "' for element '" + targetId + "'.");
        }
    }
}