package bg.tu_varna.sit.f24621627.commands;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "exit \t\t\texits the program");
    }

    @Override
    public void execute() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}