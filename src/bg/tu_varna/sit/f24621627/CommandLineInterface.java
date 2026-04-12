package bg.tu_varna.sit.f24621627;

import java.util.Map;
import java.util.Scanner;

public class CommandLineInterface {

    private XmlDocument document = new XmlDocument();
    private int newChildCounter = 1;

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
                case "set":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                            break;
                    }
                    else{
                        if (args.length < 4) {
                            System.out.println("Error: Invalid arguments. Usage: set <id> <key> <value>");
                            break;
                        } else {
                            String targetId = args[1];
                            String targetKey = args[2];
                            // combine all remaining arguments into a single value (in case the value contains spaces)
                            StringBuilder valueBuilder = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                valueBuilder.append(args[i]);
                                if (i < args.length - 1) {
                                    valueBuilder.append(" "); // add space between arguments if it's not the last one
                                }
                            }

                            String targetValue = valueBuilder.toString().replace("\"", "");
                            setCommand(targetId, targetKey, targetValue);
                        }
                    }
                    break;
                case "delete":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        break;
                    }
                    else{
                        if (args.length < 3) {
                            System.out.println("Error: Invalid arguments. Usage: delete <id> <key>");
                            break;
                        } else {
                            String targetId = args[1];
                            String targetKey = args[2];
                            deleteCommand(targetId, targetKey);
                        }
                    }
                    break;
                case "children":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        break;
                    }
                    else{
                        if (args.length < 2) {
                            System.out.println("Error: Invalid arguments. Usage: children <id>");
                            break;
                        } else {
                            String targetId = args[1];
                            executeChildren(targetId);
                        }
                    }
                    break;
                case "child":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        return;
                    }
                    else {
                        executeChild(args);
                    }
                    break;
                case "text":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        break;
                    }
                    else {
                        executeText(args);
                    }
                    break;
                case "newchild":
                    if (!document.isOpened()) {
                        System.out.println("Error: No file is currently opened.");
                        break;
                    }
                    else {
                        executeNewChild(args);
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

    private void setCommand(String targetId, String targetKey, String targetValue) {
        XmlElement elementToSet = document.getElementById(targetId);

        if (elementToSet == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            elementToSet.addAttribute(targetKey, targetValue);
            System.out.println("Successfully set attribute '" + targetKey + "' to '" + targetValue + "' for element '" + targetId + "'.");
        }
    }

    private void deleteCommand(String targetId, String targetKey) {
        XmlElement elementToDeleteFrom = document.getElementById(targetId);

        if (elementToDeleteFrom == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
        } else {
            if (elementToDeleteFrom.getAttributeByKey(targetKey) != null) {
                elementToDeleteFrom.getAttributes().remove(targetKey);
                System.out.println("Successfully deleted attribute '" + targetKey + "' from element '" + targetId + "'.");
            } else {
                System.out.println("Error: Attribute '" + targetKey + "' not found in element '" + targetId + "'.");
            }
        }
    }

    private void executeChildren(String targetId) {
        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        // check if the element has children
        if (element.getChildren() == null || element.getChildren().isEmpty()) {
            System.out.println("Element '" + targetId + "' has no children.");
        } else {
            System.out.println("Children of element '" + targetId + "':");

            // print each child with its tag and id (listed)
            for (int i = 0; i < element.getChildren().size(); i++) {
                XmlElement child = element.getChildren().get(i);

                // build a string of the child's attributes in the format key="value"
                StringBuilder attributesText = new StringBuilder();
                if (child.getAttributes() != null) {
                    for (Map.Entry<String, String> entry : child.getAttributes().entrySet()) {
                        attributesText.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
                    }
                }
                // print the child with its tag and attributes
                System.out.println((i + 1) + ". " + child.getTag() + " -> " + attributesText.toString().trim());
            }
        }
    }

    private void executeChild(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: Invalid arguments. Usage: child <id> <n>");
            return;
        }

        String targetId = args[1];
        int n;

        // 3. Опитваме се да превърнем "n" от текст в число
        try {
            n = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: The index <n> must be a valid number.");
            return;
        }

        // search for the element with the specified ID
        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        // change the user-friendly 1-based index to a 0-based index for list access
        int listIndex = n - 1;

        // check if the element has children and if the specified index is within bounds
        if (element.getChildren() == null || listIndex < 0 || listIndex >= element.getChildren().size()) {
            System.out.println("Error: Element '" + targetId + "' does not have a child at index " + n + ".");
            return;
        }

        // get the child at the specified index
        XmlElement child = element.getChildren().get(listIndex);

        // create a string of the child's attributes in the format key="value"
        StringBuilder attrText = new StringBuilder();
        if (child.getAttributes() != null) {
            for (Map.Entry<String, String> entry : child.getAttributes().entrySet()) {
                attrText.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
            }
        }

        System.out.println("Child " + n + " of element '" + targetId + "':");
        System.out.println(child.getTag() + " -> " + attrText.toString().trim());
    }

    private void executeText(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Invalid arguments. Usage: text <id>");
            return;
        }

        String targetId = args[1];

        XmlElement element = document.getElementById(targetId);

        if (element == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        // get the text content of the element and check if it's not null or empty
        String textContent = element.getTextContent();

        if (textContent != null && !textContent.trim().isEmpty()) {
            System.out.println(textContent);
        } else {
            System.out.println("Element '" + targetId + "' does not contain any text.");
        }
    }

    private void executeNewChild(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Invalid arguments. Usage: newchild <id>");
            return;
        }
        // parent id
        String targetId = args[1];
        XmlElement parent = document.getElementById(targetId);

        if (parent == null) {
            System.out.println("Error: Element with ID '" + targetId + "' not found.");
            return;
        }

        // create new element
        XmlElement newChild = new XmlElement("new_element");

        // generate id for the new child (using a simple counter to ensure uniqueness)
        String instantId = "new-" + newChildCounter++;
        newChild.setId(instantId);
        newChild.addAttribute("id", instantId);

        // add the new child to the parent's list of children
        parent.getChildren().add(newChild);
        document.getIdRegistry().put(instantId, newChild);

        System.out.println("Successfully added new child to element '" + targetId + "'. Its new ID is '" + instantId + "'.");
    }

}
