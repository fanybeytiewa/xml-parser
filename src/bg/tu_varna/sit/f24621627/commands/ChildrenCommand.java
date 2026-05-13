package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

/** Command for listing all children of an element. */
public class ChildrenCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new ChildrenCommand.
     * @param document the XML document to operate on
     */
    public ChildrenCommand(XmlDocument document) {
        super("children", "children <id>\t\tlists all children of an element");
        this.document = document;
    }

    /** Lists all children of an element. */
    @Override
    public void execute() {
        if (getArgs().length < 2) {
            System.out.println("Error: Invalid arguments. Usage: children <id>");
            return;
        }

        String targetId = getArgs()[1];
        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        if (element.getChildren() == null || element.getChildren().isEmpty()) {
            System.out.println("Element '" + targetId + "' has no children.");
        } else {
            System.out.println("Children of element '" + targetId + "':");

            for (int i = 0; i < element.getChildren().size(); i++) {
                XmlElement child = element.getChildren().get(i);
                System.out.println(document.getSerializer().serialize(child, 0).trim());
            }
        }
    }
}