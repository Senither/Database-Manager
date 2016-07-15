package com.sendev.databasemanager.plugin.bukkit.commands;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.sendev.databasemanager.Connection;
import com.sendev.databasemanager.ConnectionLevel;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.DatabaseOptions;
import com.sendev.databasemanager.factory.PluginContainer;
import com.sendev.databasemanager.plugin.bukkit.DBMPlugin;
import com.sendev.databasemanager.plugin.bukkit.contracts.DBMCommand;
import com.sendev.databasemanager.plugin.utils.sender.BukkitSender;

public class EditCommand extends DBMCommand
{
    private final String TITLE_MESSAGE = "&3&l]&3&l&m--------&3&l[ &bDBM :: %s - %s &3&l]&3&l&m--------&3&l[";

    public EditCommand(DBMPlugin plugin)
    {
        super(plugin);
    }

    @Override
    public String getPermission()
    {
        return "databasemanager.admin";
    }

    @Override
    public List<String> getTriggers()
    {
        return Arrays.asList("edit", "e");
    }

    @Override
    public List<String> getParameters()
    {
        return Arrays.asList("plugin", "options");
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Edits a plugins settings that uses DBM.");
    }

    @Override
    public boolean runPlayerCommand(Player player, String[] args)
    {
        BukkitSender sender = new BukkitSender(player);

        if (args.length == 0) {
            chat().sendMessage(sender, "%s &cYou must include a plugin name to edit!", plugin.getPrefix('4', 'c'));

            return false;
        }

        Map<String, PluginContainer> containers = plugin.getFactory().getContainers();

        for (PluginContainer container : containers.values()) {
            if (container.getName().equalsIgnoreCase(args[0])) {
                String[] parm = new String[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    parm[i - 1] = args[i];
                }

                return parseCommand(container, player, parm);
            }
        }

        chat().sendMessage(sender, "%s &4%s &cwas not found in the DBM plugin container.", plugin.getPrefix('4', 'c'), args[0]);

        return false;
    }

    private boolean parseCommand(PluginContainer container, Player player, String[] args)
    {
        if (args.length == 0) {
            return helpCommand(container, player, args);
        }

        switch (args[0].toLowerCase()) {
            case "help":
                return helpCommand(container, player, args);

            case "dump":
                return dumpCommand(container, player, args);

            case "con":
            case "conn":
            case "connection":
            case "connections":
                return connectionsCommand(container, player, args);
        }

        return false;
    }

    private boolean helpCommand(PluginContainer container, Player player, String[] args)
    {
        chat().sendMessage(new BukkitSender(player), TITLE_MESSAGE, container.getName(), "Help");

        return true;
    }

    private boolean dumpCommand(PluginContainer container, Player player, String[] args)
    {
        BukkitSender sender = new BukkitSender(player);

        chat().sendMessage(sender, TITLE_MESSAGE, container.getName(), "Dump");

        DatabaseManager instance = container.getInstance();
        DatabaseOptions options = instance.options();

        chat().sendMessage(sender, "&3%s &bconnection(s) and &3%s &bmigration(s) are registered to DBM!",
        instance.getConnections().getConnections().size(), instance.migrations().getMigrations().size());

        chat().sendMessage(sender, "&bDebug Mode    : &3%s", options.isDebugMessagesEnabled() ? "&aEnabled" : "&cDisabled");
        chat().sendMessage(sender, "&bQuery Timeout : &3%s", options.getQueryTimeout() == -1 ? "&cDisabled" : "&3" + options.getQueryTimeout() + " milliseconds");
        chat().sendMessage(sender, "&bQuery Limit     : &3%s", options.getQueryReturnLimit() == -1 ? "&cDisabled" : "&3" + options.getQueryReturnLimit());
        chat().sendMessage(sender, "&bDB-Engine      : &3%s", options.getEngine().getEngine());
        chat().sendMessage(sender, "&bDB-Prefix      : &3%s", options.getPrefix().length() == 0 ? "&7&oNone set" : "&3" + options.getPrefix());

        return true;
    }

    private boolean connectionsCommand(PluginContainer container, Player player, String[] args)
    {
        BukkitSender sender = new BukkitSender(player);

        chat().sendMessage(sender, TITLE_MESSAGE, container.getName(), "Connections");

        Map<String, Connection> connections = container.getInstance().getConnections().getConnections();

        for (String name : connections.keySet()) {
            Connection connecction = connections.get(name);

            String subName = "";
            for (int i = 0; i < name.length(); i++) {
                subName += "-";
            }

            try {
                DatabaseMetaData meta = connecction.getConnection().getConnection().getMetaData();

                chat().sendMessage(sender, "&b" + name.toUpperCase() + " &b| &3" + meta.getURL());

                ConnectionLevel level = connecction.getLevel();
                String username = "&7&oNo Hostname";
                if (meta.getUserName() != null) {
                    username = "&3" + meta.getUserName();
                }

                chat().sendMessage(sender, "&7" + subName + " &b| &3" + username + " &b: &3" + level.name() + " [&b" + level.getLevel() + "&3]");
            } catch (SQLException ex) {
            }
        }

        return true;
    }
}
