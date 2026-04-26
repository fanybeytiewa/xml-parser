package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.Map;

public class ChildCommand extends Command {
    private XmlDocument document;

    public ChildCommand(XmlDocument document) {
        super("child", "child <id> <n>\t\tprints the n-th child of an element");
        this.document = document;
    }

    @Override
    public void execute() {
        if (args.length < 3) {
            System.out.println("Error: Invalid arguments. Usage: child <id> <n>");
            return;
        }

        String targetId = args[1];
        int n;

        try {
            n = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: The index <n> must be a valid number.");
            return;
        }

        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        int listIndex = n - 1;

        if (element.getChildren() == null || listIndex < 0 || listIndex >= element.getChildren().size()) {
            System.out.println("Error: Element '" + targetId + "' does not have a child at index " + n + ".");
            return;
        }

        XmlElement child = element.getChildren().get(listIndex);
        StringBuilder attrText = new StringBuilder();
        if (child.getAttributes() != null) {
            for (Map.Entry<String, String> entry : child.getAttributes().entrySet()) {
                attrText.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
            }
        }

        System.out.println("Child " + n + " of element '" + targetId + "':");
        System.out.println(child.getTag() + " -> " + attrText.toString().trim());
    }
}