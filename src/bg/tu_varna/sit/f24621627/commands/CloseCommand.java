package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.models.XmlDocument;

/** Command for closing the currently opened document. */
public class CloseCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;

    /**
     * Creates a new CloseCommand.
     * @param document the XML document to operate on
     */
    public CloseCommand(XmlDocument document) {
        super("close", "", "closes currently opened file");
        this.document = document;
    }

    /** Closes the currently opened document. */
    @Override
    public void execute() {
        document.close();
    }
}