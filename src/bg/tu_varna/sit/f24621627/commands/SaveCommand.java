package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XmlDocument;

public class SaveCommand extends Command {
    private XmlDocument document;

    public SaveCommand(XmlDocument document) {
        super("save", "save \t\t\t saves the currently opened file (or 'save as <file>')");
        this.document = document;
    }

    @Override
    public void execute() {
        if (getArgs().length > 1 && getArgs()[1].equalsIgnoreCase("as")) {
            if (getArgs().length < 3) {
                System.out.println("Error: Please provide a file path. Example: save as newfile.xml");
            } else {
                String path = getArgs()[2].replace("\"", "");
                document.saveAs(path);
            }
        } else {
            document.save();
        }
    }
}