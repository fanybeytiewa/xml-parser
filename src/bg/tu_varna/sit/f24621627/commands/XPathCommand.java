package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.XPathService;
import bg.tu_varna.sit.f24621627.XmlDocument;
import bg.tu_varna.sit.f24621627.XmlElement;

import java.util.List;

public class XPathCommand extends Command {
    private XmlDocument document;
    private XPathService xpathService;

    public XPathCommand(XmlDocument document, XPathService xpathService) {
        super("xpath", "xpath <id> <path>\texecutes XPath query");
        this.document = document;
        this.xpathService = xpathService;
    }

    @Override
    public void execute() {
        if (args.length < 3) {
            System.out.println("Usage: xpath <id> <XPath>");
            return;
        }

        String startId = args[1];
        String path = args[2];

        XmlElement startNode = document.getElementById(startId);
        if (startNode == null) {
            System.out.println("Error: Element with ID '" + startId + "' not found.");
            return;
        }

        List<String> results = xpathService.evaluate(startNode, path);

        if (results.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            for (String value : results) {
                System.out.println(value);
            }
        }
    }
}