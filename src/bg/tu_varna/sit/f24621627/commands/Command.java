package bg.tu_varna.sit.f24621627.commands;

/**
 * Abstract class for a CLI command.
 * Each concrete command extends this class and implements the {@link #execute()}.
 * Uses the Command Pattern.
 */
public abstract class Command {
    /** The command name used for lookup (e.g. "open", "save"). */
    private String name;

    /** Description text displayed in the help menu. */
    /** The syntax usage of the command. */
    private String argsSyntax;

    /** Description text displayed in the help menu. */
    private String description;

    /** Command line arguments passed during execution. */
    private String[] args;

    /**
     * Creates a new command with a name and description.
     * @param name the command name ( e.g. "open", "save")
     * @param description description for the help menu
     */
    public Command(String name, String argsSyntax, String description) {
        this.name = name;
        this.argsSyntax = argsSyntax;
        this.description = description;
    }

    /** @return the command name */
    public String getName() { return name; }

    /** @return the command description */
    public String getDescription() { return description; }

    /** @return the command syntax */
    public String getSyntax() { return (argsSyntax == null || argsSyntax.isEmpty()) ? name : name + " " + argsSyntax; }

    /** @return the arguments passed to the command */
    public String[] getArgs() { return args; }

    /**
     * Sets the command arguments before execution.
     * @param args array of command line arguments
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    /** Executes the command. Each subclass implements its own logic. */
    public abstract void execute();
}