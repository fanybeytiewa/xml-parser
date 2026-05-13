package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

/** Command for printing formatted XML to the console. */
public class PrintCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new PrintCommand.
     * @param document the XML document to operate on
     */
    public PrintCommand(XmlDocument document) {
        super("print", "print\t\t\tprints the content of the XML file");
        this.document = document;
    }

    /** Prints the XML content to the console. */
    @Override
    public void execute() {
        if (document.getRootElement() == null) {
            System.out.println("Error: No valid XML structure to print.");
            return;
        }
        System.out.println(document.getSerializer().serialize(document.getRootElement(), 0));
    }
}