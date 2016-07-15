package com.sendev.databasemanager.plugin.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.bungee.contracts.DBMCommand;
import com.sendev.databasemanager.plugin.utils.StringMatcher;
import com.sendev.databasemanager.plugin.utils.sender.BungeeSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CommandHandler extends Command
{
    private final DBMPlugin plugin;

    private final List<DBMCommand> commands;
    private DBMCommand defaultCommand = null;

    public CommandHandler(DBMPlugin plugin)
    {
        super("bungeedatabasemanager", null, new String[]{"bungeedbmanager", "bdbmanager", "bungeedbm", "bdbm"});

        this.plugin = plugin;

        commands = new ArrayList<>();
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

        this.defaultCommand = command;
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
    public void execute(CommandSender sender, String[] args)
    {
        BungeeSender player = new BungeeSender(sender);

        if (args.length == 0) {
            if (defaultCommand == null) {
                plugin.getChat().sendMessage(player, "&cThere doesn't seem to be a default command registered, try and use some arguments.");

                return;
            }

            if (defaultCommand.getPermission() != null && !sender.hasPermission(defaultCommand.getPermission())) {
                plugin.getChat().missingPermission(player, defaultCommand.getPermission());

                return;
            }

            defaultCommand.runCommand(sender, args);

            return;
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
                    plugin.getChat().missingPermission(player, cmd.getPermission());

                    return;
                }

                cmd.runCommand(sender, args);

                return;
            }
        }

        if (!sender.hasPermission("databasemanager.admin")) {
            plugin.getChat().missingPermission(player, "databasemanager.admin");

            return;
        }

        List<String> commandTriggers = new ArrayList<>();

        commands.stream().filter(( cmd ) -> !(cmd.getTriggers().isEmpty())).forEach(( cmd ) -> {
            commandTriggers.add(cmd.getTriggers().get(0));
        });

        String match = StringMatcher.match(commandTrigger, commandTriggers).getMatch();

        plugin.getChat().sendMessage(player, "%s &4%s &cwas not found! Did you mean...", plugin.getPrefix('4', 'c'), commandTrigger);
        plugin.getChat().sendMessage(player, "&4/&cDBM &4[&c%s&4]", match);
    }
}
