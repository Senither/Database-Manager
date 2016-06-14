package com.sendev.databasemanager.plugin.contracts;

import com.sendev.databasemanager.plugin.DBMPlugin;
import com.sendev.databasemanager.plugin.utils.ChatFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class DBMCommand
{
    protected final DBMPlugin plugin;

    public DBMCommand(DBMPlugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean hasPermission()
    {
        return getPermission() != null;
    }

    public String getPermission()
    {
        return null;
    }

    public List<String> getTriggers()
    {
        return new ArrayList<>();
    }

    public List<String> getDescription()
    {
        return new ArrayList<>();
    }

    public List<String> getParameters()
    {
        return new ArrayList<>();
    }

    public boolean displayOnHelp()
    {
        return true;
    }

    public final boolean sendDescriptionMessage(Player player)
    {
        return sendDescriptionMessage(player, true);
    }

    public final boolean sendDescriptionMessage(Player player, boolean sendDescription)
    {
        if (getTriggers().isEmpty()) {
            return false;
        }

        if (getPermission() != null && !player.hasPermission(getPermission())) {
            return false;
        }

        String command = String.format("&3/&bDBM %s ", getTriggers().get(0));

        if (!getParameters().isEmpty()) {
            command = getParameters().stream().map(( parameter )
            -> String.format("&3[&b%s&3] ", parameter)
            ).reduce(command, String::concat);
        }

        plugin.getChat().sendMessage(player, command.trim());

        if (sendDescription) {
            getDescription().stream().forEach(( line ) -> {
                plugin.getChat().sendMessage(player, " &3&l&m*&7 " + line);
            });
        }

        return true;
    }

    protected final void sendMessage(Player player, String message)
    {
        chat().sendMessage(player, "%s &b%s", plugin.getPrefix('3', 'b'), message);
    }

    protected final void sendMessage(CommandSender sender, String message)
    {
        chat().sendMessage(sender, "%s &b%s", plugin.getPrefix('3', 'b'), message);
    }

    protected final void sendErrorMessage(Player player, String message)
    {
        chat().sendMessage(player, "%s &c%s", plugin.getPrefix('4', 'c'), message);
    }

    protected final ChatFormatter chat()
    {
        return plugin.getChat();
    }

    public boolean runPlayerCommand(Player player, String[] args)
    {
        return false;
    }

    public boolean runConsoleCommand(CommandSender sender, String[] args)
    {
        return false;
    }
}
