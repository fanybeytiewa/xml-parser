package bg.tu_varna.sit.f24621627;

import bg.tu_varna.sit.f24621627.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Command line interface (CLI) for working with XML documents.
 * Registers available commands and processes user input from the console.
 */
public class CommandLineInterface {

    /** The XML document being operated on. */
    private XmlDocument document = new XmlDocument();

    /** Service for evaluating XPath queries. */
    private XPathService xpathService = new XPathService();

    /** Map of command names to their implementations. */
    private Map<String, Command> commands = new HashMap<>();

    /** Registers all available commands in the command map. */
    public CommandLineInterface() {
        // Register all command objects in the map
        commands.put("open", new OpenCommand(document));
        commands.put("close", new CloseCommand(document));
        commands.put("save", new SaveCommand(document));
        commands.put("print", new PrintCommand(document));
        commands.put("select", new SelectCommand(document));
        commands.put("set", new SetCommand(document));
        commands.put("delete", new DeleteCommand(document));
        commands.put("children", new ChildrenCommand(document));
        commands.put("child", new ChildCommand(document));
        commands.put("text", new TextCommand(document));
        commands.put("newchild", new NewChildCommand(document));
        commands.put("xpath", new XPathCommand(document, xpathService));
        commands.put("exit", new ExitCommand());

        // Pass the commands map to HelpCommand so it can generate the menu
        commands.put("help", new HelpCommand(commands));
    }

    /** Starts the command reading loop from the user. */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to XML Parser. Type 'help' for commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            // Split into max 4 parts for the general case (important for set)
            String[] args = input.split("\\s+", 4);
            String commandName = args[0].toLowerCase();

            Command command = commands.get(commandName);
            if (command == null) {
                System.out.println("Unknown command. Type 'help' to see available commands.");
                continue;
            }

            // If the command is open, take everything after the command name as one argument
            if (commandName.equals("open")) {
                String parts[] = input.split("\\s+", 2); // Split into "open" and "everything else"
                if (parts.length > 1) {
                    args = new String[]{"open", parts[1]};
                }
            }

            // If the command is save as, take the path as one argument (may contain spaces)
            if (commandName.equals("save") && args.length > 1 && args[1].equalsIgnoreCase("as")) {
                String[] parts = input.split("\\s+", 3); // Split into "save", "as" and "everything else"
                if (parts.length >= 3) {
                    args = new String[]{"save", "as", parts[2]};
                }
            }

            if (!commandName.equals("open") && !commandName.equals("help") &&
                    !commandName.equals("exit") && !document.isOpened()) {
                System.out.println("Error: No file is currently opened.");
                continue;
            }

            command.setArgs(args);
            command.execute();
        }
    }
}