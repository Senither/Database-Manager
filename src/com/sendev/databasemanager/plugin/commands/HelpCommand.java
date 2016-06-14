package com.sendev.databasemanager.plugin.commands;

import com.sendev.databasemanager.plugin.DBMPlugin;
import com.sendev.databasemanager.plugin.contracts.DBMCommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class HelpCommand extends DBMCommand
{
    public HelpCommand(DBMPlugin plugin)
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
        return Arrays.asList("help", "commands", "command");
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Displays all commands avaliable in DBM.");
    }

    @Override
    public boolean runPlayerCommand(Player player, String[] args)
    {
        PluginDescriptionFile desc = plugin.getDescription();

        chat().sendMessage(player, "&3&l]&3&l&m--------&3&l[ &b%s &3v&7%s &3&l]&3&l&m--------&3&l[",
        "Database Manager", desc.getVersion()
        );

        plugin.getCommand().getCommands().stream().filter(( command ) -> !(!command.displayOnHelp())).forEach(( command ) -> {
            command.sendDescriptionMessage(player);
        });

        return false;
    }
}
