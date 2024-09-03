package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.util.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandHandlerDispatcher {
    private static String commandNominator = ";";
    private static final List<Command> commands = new ArrayList<>();

    public static String getCommandNominator() {
        return commandNominator;
    }
    public static List<Command> getCommands() {
        return commands;
    }
    public static void handleCommand(String command) {
        command = stripCommandNominator(command);
        String[] formattedCommand = formatCommand(command);
        if (formattedCommand.length < 1) return;

        boolean wasHandled = false;

        for (Command c : commands) {
            if (c.getStart().equals(formattedCommand[0])) {
                c.handle(formattedCommand);
                wasHandled = true;
            }
        }
        if (!wasHandled) {
            ChatUtils.sendError("Unknown command : &&a" + formattedCommand[0]);
        }
    }
    public static void initCommands() {
        commands.add(new HelpCommand());
        commands.add(new LoopCommand());
        commands.add(new DamageCommand());
        commands.add(new WaypointsCommand());
    }
    public static String stripCommandNominator(String string) {
        if (string.startsWith(getCommandNominator())) return string.substring(getCommandNominator().length());
        return string;
    }
    public static String[] formatCommand(String string) {
        return string.split(" ");
    }

}
