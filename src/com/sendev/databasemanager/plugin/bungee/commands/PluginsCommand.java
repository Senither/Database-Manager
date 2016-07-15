package com.sendev.databasemanager.plugin.bungee.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sendev.databasemanager.factory.PluginContainer;
import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.bungee.contracts.DBMCommand;
import com.sendev.databasemanager.plugin.utils.sender.BungeeSender;
import net.md_5.bungee.api.CommandSender;

public class PluginsCommand extends DBMCommand
{
    public PluginsCommand(DBMPlugin plugin)
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
        return Arrays.asList("plugins", "stats", "plugin", "stat");
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Displays all plugins using DBM, and some stats about them.");
    }

    @Override
    public boolean runCommand(CommandSender player, String[] args)
    {
        BungeeSender sender = new BungeeSender(player);

        chat().sendMessage(sender, "&3&l]&3&l&m--------&3&l[ &bDatabase Manager : Stats &3&l]&3&l&m--------&3&l[");

        Map<String, PluginContainer> containers = plugin.getFactory().getContainers();

        if (containers.isEmpty()) {
            chat().sendMessage(sender, "&7There doesn't seem to be any plugins currently using DBM.");

            return false;
        }

        containers.values().stream().forEach(( container ) -> {
            int connSize = container.getInstance().getConnections().getConnections().size();
            int migrSize = container.getInstance().migrations().getMigrations().size();

            chat().sendMessage(sender, "&3&m&l ]&3[ &7&o&l%s &7with &l%s &7connection%s and &l%s &7migration%s", container.getName(),
            connSize, (connSize == 1) ? "" : "s",
            migrSize, (migrSize == 1) ? "" : "s");
        });

        return true;
    }
}
