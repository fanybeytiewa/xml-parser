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
                // open, close...
                default:
                    System.out.println("Unknown command. Type 'help' to see available commands.");
            }
        }
        scanner.close();
    }

    private void printHelp() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>  \t opens <file>");
        System.out.println("close        \t closes currently opened file");
        System.out.println("exit         \t exits the program");
        System.out.println("save         \t saves the currently opened file");
        System.out.println("save as <file> \tsaves the currently opened file in <file>");
        System.out.println("help         \t prints this information");
    }
}
