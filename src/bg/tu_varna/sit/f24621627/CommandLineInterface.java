package bg.tu_varna.sit.f24621627;

import bg.tu_varna.sit.f24621627.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLineInterface {

    private XmlDocument document = new XmlDocument();
    private XPathService xpathService = new XPathService();
    private Map<String, Command> commands = new HashMap<>();

    public CommandLineInterface() {
        // Регистрираме всички обекти-команди в речника
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

        // Подаваме мапа на HelpCommand, за да може сам да си генерира менюто
        commands.put("help", new HelpCommand(commands));
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to XML Parser. Type 'help' for commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] args = input.split(" ");
            String commandName = args[0].toLowerCase();

            Command command = commands.get(commandName);

            if (command == null) {
                System.out.println("Unknown command. Type 'help' to see available commands.");
                continue;
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