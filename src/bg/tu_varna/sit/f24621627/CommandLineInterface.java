package bg.tu_varna.sit.f24621627;

import java.util.Scanner;

public class CommandLineInterface {

    private XmlDocument document = new XmlDocument();

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("Welcome to XML Parser. Type 'help' for commands.");

        while (isRunning) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] args = input.split(" ");
            String command = args[0].toLowerCase();

            switch (command) {
                case "open":
                    if (args.length < 2) {
                        System.out.println("Error: Please provide a file path.");}
                    else {
                        document.open(args[1]);}
                    break;
                case "close":
                    document.close();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    System.out.println("Exiting the program...");
                    isRunning = false;
                    break;
                case "save":
                    if (args.length > 1 && args[1].equalsIgnoreCase("as")) { // "save as" check
                        if (args.length < 3) {
                            System.out.println("Error: Please provide a file path. Example: save as newfile.xml");
                        } else {
                            String path = args[2].replace("\"", "");
                            document.saveAs(path);
                        }
                    } else {
                        document.save();
                    }
                    break;
                case "print":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                    } else {
                        System.out.println(document.getRootElement().toXml(0));
                    }
                    break;
                case "select":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        break;
                    }
                    if (args.length < 3) {
                        System.out.println("Error: Invalid arguments. Usage: select <id> <key>");
                        break;
                    }
                    else {
                        String targetId = args[1];
                        String targetKey = args[2];
                        selectCommand(targetId, targetKey);
                    }
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' to see available commands.");

            }
        }
        scanner.close();
    }

    private void printHelp() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>\t\t opens <file>");
        System.out.println("close\t\t\t closes currently opened file");
        System.out.println("exit \t\t\t exits the program");
        System.out.println("save \t\t\t saves the currently opened file");
        System.out.println("save as <file>\tsaves the currently opened file in <file>");
        System.out.println("print\t\t\tprints the content of the XML file");
        System.out.println("help \t\t\tprints this information");
    }

    private void selectCommand(String targetId, String targetKey) {
        // search for the element with the specified ID
        XmlElement elementToSelect = document.getElementById(targetId);

        if (elementToSelect == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            // search for the specified attribute key in the found element
            String attributeValue = elementToSelect.getAttributeByKey(targetKey);

            if (attributeValue != null) {
                System.out.println(attributeValue);
            } else {
                System.out.println("Error: Attribute '" + targetKey + "' not found in element '" + targetId + "'.");
            }
        }
    }

}
