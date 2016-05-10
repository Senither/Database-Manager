package com.sendev.databasemanager;

import java.security.InvalidParameterException;
import org.bukkit.plugin.Plugin;

public class DatabaseFactory
{

    public static DatabaseManager createNewInstance(Plugin plugin)
    {
        if (plugin == null) {
            throw new InvalidParameterException("The plugin parameter must be an instance of the Bukkit Plugin instance!");
        }

        return new DatabaseManager(plugin);
    }
}
