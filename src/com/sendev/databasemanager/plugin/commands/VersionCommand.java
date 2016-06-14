package com.sendev.databasemanager.plugin.commands;

import com.sendev.databasemanager.plugin.DBMPlugin;
import com.sendev.databasemanager.plugin.contracts.DBMCommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VersionCommand extends DBMCommand
{
    public VersionCommand(DBMPlugin plugin)
    {
        super(plugin);
    }

    @Override
    public List<String> getTriggers()
    {
        return Arrays.asList("version", "v");
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Displays the version and a few help links for DBM.");
    }

    @Override
    public boolean runPlayerCommand(Player player, String[] args)
    {
        String version = plugin.getDescription().getVersion();

        sendMessage(player, "&7The server is running DatabaseManager &3v" + version);
        sendMessage(player, "&7Created by &3Alexis Tan &b[&3Senither&b]");
        sendMessage(player, "&7Site: &3https://bitbucket.org/Senither/database-manager");

        return true;
    }

    @Override
    public boolean runConsoleCommand(CommandSender sender, String[] args)
    {
        String version = plugin.getDescription().getVersion();

        chat().sendMessage(sender, "The server is running DatabaseManager v" + version);
        chat().sendMessage(sender, "Created by Alexis Tan [Senither] (https://bitbucket.org/Senither/database-manager)");

        return true;
    }
}