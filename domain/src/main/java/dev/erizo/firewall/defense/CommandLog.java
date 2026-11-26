package dev.erizo.firewall.defense;

import java.util.ArrayList;
import java.util.List;

/** The ordered record of the player's commands, used to replay a match. */
public final class CommandLog {

    private final List<MatchCommand> commands;

    public CommandLog() {
        this.commands = new ArrayList<>();
    }

    /** Records a command in the order it was issued. */
    public void record(MatchCommand command) {
        commands.add(command);
    }

    /** The commands due at the given tick, in the order they were recorded. */
    public List<MatchCommand> commandsAt(int tick) {
        return commands.stream().filter(command -> command.tick() == tick).toList();
    }

    public List<MatchCommand> all() {
        return List.copyOf(commands);
    }

    /** The last tick any command is scheduled for, or zero when empty. */
    public int lastCommandTick() {
        return commands.stream().mapToInt(MatchCommand::tick).max().orElse(0);
    }
}
