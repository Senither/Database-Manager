package com.sendev.databasemanager.plugin.bungee.commands;

import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.bungee.contracts.DBMCommand;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.CommandSender;

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
    public boolean runCommand(CommandSender player, String[] args)
    {
        String version = plugin.getDescription().getVersion();

        sendMessage(player, "&7The proxy is running DatabaseManager &3v" + version);
        sendMessage(player, "&7Created by &3Alexis Tan &b[&3Senither&b]");
        sendMessage(player, "&7Site: &3https://bitbucket.org/Senither/database-manager");

        return true;
    }
}
