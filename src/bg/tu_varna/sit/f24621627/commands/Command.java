package bg.tu_varna.sit.f24621627.commands;

public abstract class Command {
    private String name;
    private String description;
    private String[] args;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String[] getArgs() { return args; }

    public void setArgs(String[] args) {
        this.args = args;
    }



    public abstract void execute();
}