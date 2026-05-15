package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.models.XmlDocument;
import bg.tu_varna.sit.f24621627.models.XmlElement;

/** Command for accessing the n-th child of an element. */
public class ChildCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new ChildCommand.
     * @param document the XML document to operate on
     */
    public ChildCommand(XmlDocument document) {
        super("child", "<id> <n>", "prints the n-th child of an element");
        this.document = document;
    }

    /** Prints the n-th child of an element. */
    @Override
    public void execute() {
        if (getArgs().length < 3) {
            System.out.println("Error: Invalid arguments. Usage: child <id> <n>");
            return;
        }

        String targetId = getArgs()[1];
        int n;

        try {
            n = Integer.parseInt(getArgs()[2]);
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

        System.out.println("Child " + n + " of element '" + targetId + "':");
        System.out.println(child.toString());
    }
}