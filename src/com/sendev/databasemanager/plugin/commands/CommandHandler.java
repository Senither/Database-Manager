package com.sendev.databasemanager.plugin.commands;

import com.sendev.databasemanager.plugin.DBMPlugin;
import com.sendev.databasemanager.plugin.contracts.DBMCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor
{
    private final DBMPlugin plugin;

    private final List<DBMCommand> commands;
    private final List<DBMCommand> defaultCommands;

    public CommandHandler(DBMPlugin plugin)
    {
        this.plugin = plugin;

        commands = new ArrayList<>();
        defaultCommands = new ArrayList<>();
    }

    /**
     * Registers a DBM command to the command lists as a normal command.
     *
     * @param command the command to register
     */
    public void registerCommand(DBMCommand command)
    {
        if (commands.contains(command)) {
            return;
        }

        commands.add(command);
    }

    /**
     * Registers a DBM command to the command lists, if the defaultCommand value is set
     * to true, the command will also be registered in the default commands list, if
     * defaultCommand is set to false, it will be registered as a normal command.
     *
     * @param command        the command to register
     * @param defaultCommand the default value for the command
     */
    public void registerCommand(DBMCommand command, boolean defaultCommand)
    {
        registerCommand(command);

        if (!defaultCommand) {
            return;
        }

        if (defaultCommands.contains(command)) {
            return;
        }

        defaultCommands.add(command);
    }

    /**
     * Gets all of the registered commands, both normal and default.
     *
     * @return all of the registered commands
     */
    public List<DBMCommand> getCommands()
    {
        return commands;
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0) {
            for (DBMCommand cmd : defaultCommands) {
                if (!cmd.hasPermission()) {
                    return parseCommand(cmd, sender, args);
                }

                if (sender.hasPermission(cmd.getPermission())) {
                    return parseCommand(cmd, sender, args);
                }
            }

            return false;
        }

        String commandTrigger = args[0];

        String[] parm = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            parm[i - 1] = args[i];
        }

        for (DBMCommand cmd : commands) {
            for (String trigger : cmd.getTriggers()) {
                if (!trigger.equalsIgnoreCase(commandTrigger)) {
                    continue;
                }

                if (cmd.hasPermission() && !sender.hasPermission(cmd.getPermission())) {
                    plugin.getChat().missingPermission(sender, cmd.getPermission());

                    return false;
                }

                return parseCommand(cmd, sender, parm);
            }
        }

        return false;
    }

    private boolean parseCommand(DBMCommand command, CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player)) {
            return command.runConsoleCommand(sender, args);
        }

        Player player = (Player) sender;

        return command.runPlayerCommand(player, args);
    }
}
