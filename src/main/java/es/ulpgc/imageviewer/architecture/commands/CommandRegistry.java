package es.ulpgc.imageviewer.architecture.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private static CommandRegistry instance;

    private final Map<CommandName, Command> commands = new HashMap<>();

    public static CommandRegistry getInstance() {
        if (instance == null)
            instance = new CommandRegistry();
        return instance;
    }

    private CommandRegistry() { }

    public CommandRegistry register(CommandName name, Command command) {
        commands.put(name, command);
        return this;
    }

    public Command get(CommandName name) {
        return commands.get(name);
    }
}
