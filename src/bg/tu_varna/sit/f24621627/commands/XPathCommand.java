package bg.tu_varna.sit.f24621627.commands;

import bg.tu_varna.sit.f24621627.xpath.XPathService;
import bg.tu_varna.sit.f24621627.models.XmlDocument;
import bg.tu_varna.sit.f24621627.models.XmlElement;

import java.util.List;

/** Command for executing an XPath query on an XML document. */
public class XPathCommand extends Command {
    /** Reference to the XML document being operated on. */
    private XmlDocument document;
    /** Service for evaluating XPath expressions. */
    private XPathService xpathService;

    /**
     * Creates a new XPathCommand.
     * @param document the XML document to operate on
     * @param xpathService the XPath evaluation service
     */
    public XPathCommand(XmlDocument document, XPathService xpathService) {
        super("xpath", "<id> <path>", "executes XPath query");
        this.document = document;
        this.xpathService = xpathService;
    }

    /** Evaluates an XPath query and prints the results. */
    @Override
    public void execute() {
        if (getArgs().length < 3) {
            System.out.println("Usage: xpath <id> <XPath>");
            return;
        }

        String startId = getArgs()[1];
        String path = getArgs()[2];

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