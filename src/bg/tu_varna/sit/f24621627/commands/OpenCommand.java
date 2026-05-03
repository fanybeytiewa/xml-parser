package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

public class OpenCommand extends Command {
    private XmlDocument document;

    public OpenCommand(XmlDocument document) {
        super("open", "open <file>\t\t opens <file>");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length < 2) {
            System.out.println("Error: Please provide a file path.");
        } else {
            document.open(getArgs()[1]);
        }
    }
}