package com.sendev.databasemanager.plugin.type;

import com.sendev.databasemanager.exceptions.InvalidPluginException;
import com.sendev.databasemanager.plugin.contracts.DatabasePlugin;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin implements DatabasePlugin
{
    private Plugin plugin;

    @Override
    public void parse(Object plugin) throws InvalidPluginException
    {
        if (!(plugin instanceof JavaPlugin)) {
            throw new InvalidPluginException("Invalid plugin given, the plugin object is not an instance of bukkits JavaPlugin");
        }

        this.plugin = (Plugin) plugin;
    }

    @Override
    public String getName()
    {
        return plugin.getDescription().getName();
    }

    @Override
    public String getMain()
    {
        return plugin.getDescription().getMain();
    }

    @Override
    public Logger getLogger()
    {
        return plugin.getLogger();
    }
}
