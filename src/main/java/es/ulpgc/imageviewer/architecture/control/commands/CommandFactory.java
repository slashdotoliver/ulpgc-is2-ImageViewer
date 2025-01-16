package es.ulpgc.imageviewer.architecture.control.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private final Map<CommandName, Command> commands = new HashMap<>();

    public void register(CommandName name, Command command) {
        commands.put(name, command);
    }

    public Command get(CommandName name) {
        return commands.get(name);
    }
}
