package bg.tu_varna.sit.f24621627;

import bg.tu_varna.sit.f24621627.commands.*;
import bg.tu_varna.sit.f24621627.models.XmlDocument;
import bg.tu_varna.sit.f24621627.xpath.XPathService;

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

            String[] args = parseArguments(input);
            String commandName = args[0].toLowerCase();

            Command command = commands.get(commandName);
            if (command == null) {
                System.out.println("Unknown command. Type 'help' to see available commands.");
                continue;
            }

            if (requiresOpenDocument(commandName) && !document.isOpened()) {
                System.out.println("Error: No file is currently opened.");
                continue;
            }

            command.setArgs(args);
            command.execute();
        }
    }

    /**
     * Parses the raw user input into an array of arguments, handling special cases.
     * @param input the raw user input
     * @return an array of parsed arguments
     */
    private String[] parseArguments(String input) {
        // Split into max 4 parts for the general case (important for set)
        String[] args = input.split("\\s+", 4);
        String commandName = args[0].toLowerCase();

        // If the command is open, take everything after the command name as one argument
        if (commandName.equals("open")) {
            String[] parts = input.split("\\s+", 2);
            if (parts.length > 1) {
                return new String[]{"open", parts[1]};
            }
        }

        // If the command is save as, take the path as one argument (may contain spaces)
        if (commandName.equals("save") && args.length > 1 && args[1].equalsIgnoreCase("as")) {
            String[] parts = input.split("\\s+", 3);
            if (parts.length >= 3) {
                return new String[]{"save", "as", parts[2]};
            }
        }

        // Специален случай за xpath: разделяме само на 3 части (команда, ID, Път)
        if (commandName.equals("xpath")) {
            String[] xpathParts = input.split("\\s+", 3);
            if (xpathParts.length >= 3) {
                return xpathParts;
            }
        }

        return args;
    }

    /**
     * Checks if a command requires an opened document to be executed.
     * @param commandName the name of the command
     * @return true if it requires an opened document, false otherwise
     */
    private boolean requiresOpenDocument(String commandName) {
        return !commandName.equals("open") && !commandName.equals("help") && !commandName.equals("exit");
    }
}