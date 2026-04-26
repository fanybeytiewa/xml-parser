package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

public class CloseCommand extends Command {
    private XmlDocument document;

    public CloseCommand(XmlDocument document) {
        super("close", "close\t\t\t closes currently opened file");
        this.document = document;
    }

    @Override
    public void execute() {
        document.close();
    }
}