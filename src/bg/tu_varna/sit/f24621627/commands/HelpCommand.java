package bg.tu_varna.sit.f24621627.commands;

import java.util.Map;

public class HelpCommand extends Command {
    private Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        super("help", "help \t\t\tprints this information");
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("The following commands are supported:");
        for (Command cmd : commands.values()) {
            System.out.println(cmd.getDescription());
        }
    }
}