package bg.tu_varna.sit.f24621627.commands;

/**
 * Abstract class for a CLI command.
 * Each concrete command extends this class and implements the {@link #execute()}.
 * Uses the Command Pattern.
 */
public abstract class Command {
    private String name;
    private String description;
    private String[] args;

    /**
     * Creates a new command with a name and description.
     * @param name the command name ( e.g. "open", "save")
     * @param description description for the help menu
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /** @return the command name */
    public String getName() { return name; }

    /** @return the command description */
    public String getDescription() { return description; }

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