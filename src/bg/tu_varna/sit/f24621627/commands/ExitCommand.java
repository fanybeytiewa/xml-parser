package bg.tu_varna.sit.f24621627.commands;

/** Command for exiting the program. */
public class ExitCommand extends Command {
    /**
     * Creates a new ExitCommand.
     */
    public ExitCommand() {
        super("exit", "exit \t\t\texits the program");
    }

    /** Terminates the application. */
    @Override
    public void execute() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}