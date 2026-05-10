package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

public class PrintCommand extends Command {
    private XmlDocument document;

    public PrintCommand(XmlDocument document) {
        super("print", "print\t\t\tprints the content of the XML file");
        this.document = document;
    }

    @Override
    public void execute() {
        if (document.getRootElement() == null) {
            System.out.println("Error: No valid XML structure to print.");
            return;
        }
        System.out.println(document.getRootElement().toXml(0));
    }
}