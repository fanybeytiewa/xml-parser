package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

public class NewChildCommand extends Command {
    private XmlDocument document;
    private int newChildCounter = 1; // Броячът за нови ID-та живее тук

    public NewChildCommand(XmlDocument document) {
        super("newchild", "newchild <id>\t\tadds a new child element");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length < 2) {
            System.out.println("Error: Invalid arguments. Usage: newchild <id>");
            return;
        }

        String targetId = getArgs()[1];
        XmlElement parent = document.getElementById(targetId);

        if (parent == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        // create new element
        XmlElement newChild = new XmlElement("new_element");

        // generate id for the new child
        String instantId = "new-" + newChildCounter++;
        newChild.setId(instantId);
        newChild.addAttribute("id", instantId);

        // add the new child to the parent's list of children
        parent.getChildren().add(newChild);
        document.getIdRegistry().put(instantId, newChild);

        System.out.println("Successfully added new child to element '" + targetId + "'. Its new ID is '" + instantId + "'.");
    }
}