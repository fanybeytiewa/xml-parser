package bg.tu_varna.sit.f24621627.commands;

import java.util.Map;

/** Command for displaying help information about all available commands. */
public class HelpCommand extends Command {
    /** Map of all available commands for generating help text. */
    private Map<String, Command> commands;

    /**
     * Creates a new HelpCommand.
     * @param commands map of available commands
     */
    public HelpCommand(Map<String, Command> commands) {
        super("help", "", "prints this information");
        this.commands = commands;
    }

    /** Displays help information about all available commands. */
    @Override
    public void execute() {
        System.out.println("The following commands are supported:");
        for (Command cmd : commands.values()) {
            System.out.printf("%-25s - %s%n", cmd.getSyntax(), cmd.getDescription());
        }
    }
}