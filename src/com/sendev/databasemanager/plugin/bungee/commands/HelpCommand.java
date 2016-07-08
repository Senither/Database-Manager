package com.sendev.databasemanager.plugin.bungee.commands;

import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.bungee.contracts.DBMCommand;
import com.sendev.databasemanager.plugin.utils.sender.BungeeSender;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.PluginDescription;

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
    public boolean runCommand(CommandSender player, String[] args)
    {
        BungeeSender sender = new BungeeSender(player);

        PluginDescription desc = plugin.getDescription();

        chat().sendMessage(sender, "&3&l]&3&l&m------&3&l[ &b%s &3v&7%s &3&l]&3&l&m------&3&l[",
        "BungeeCord Database Manager", desc.getVersion()
        );

        plugin.getCommand().getCommands().stream().filter(( command ) -> !(!command.displayOnHelp())).forEach(( command ) -> {
            command.sendCommandInformationMessage(player);
        });

        return false;
    }
}
