package bg.tu_varna.sit.f24621627;

/**
 * Main class of the XML Parser application.
 * Starts the command line interface for working with XML files.
 */
public class Application {

    /**
     * Entry point of the program.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run();

    }
}
