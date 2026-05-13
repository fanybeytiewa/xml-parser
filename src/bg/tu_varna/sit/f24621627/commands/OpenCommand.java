package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

/** Command for opening an XML file. */
public class OpenCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new OpenCommand.
     * @param document the XML document to operate on
     */
    public OpenCommand(XmlDocument document) {
        super("open", "open <file>\t\t opens <file>");
        this.document = document;
    }

    /** Opens the specified XML file. */
    @Override
    public void execute() {
        if (getArgs().length < 2) {
            System.out.println("Error: Please provide a file path.");
        } else {
            document.open(getArgs()[1]);
        }
    }
}