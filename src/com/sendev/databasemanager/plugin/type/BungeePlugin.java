package com.sendev.databasemanager.plugin.type;

import java.util.logging.Logger;

import com.sendev.databasemanager.exceptions.InvalidPluginException;
import com.sendev.databasemanager.plugin.contracts.DatabasePlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin implements DatabasePlugin
{
    private Plugin plugin;

    @Override
    public void parse(Object plugin) throws InvalidPluginException
    {
        if (!(plugin instanceof Plugin)) {
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
