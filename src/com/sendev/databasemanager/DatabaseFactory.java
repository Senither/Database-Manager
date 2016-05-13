package com.sendev.databasemanager;

import java.security.InvalidParameterException;
import org.bukkit.plugin.Plugin;

public class DatabaseFactory
{

    /**
     * Creates a new Database Manager(DBM) instance, allowing you to communicate with databases easier,
     * using the DBM also gives you access to the Database Schema and Query Builder which makes it
     * even easier to create, delete, modify and update database records.
     *
     * @see com.sendev.databasemanager.query.QueryBuilder
     * @see com.sendev.databasemanager.schema.Schema
     *
     * @param plugin The instance of the plugin that are going to use the DBM.
     *
     * @return A new a fresh instance of the Database Manager.
     */
    public static DatabaseManager createNewInstance(Plugin plugin)
    {
        if (plugin == null) {
            throw new InvalidParameterException("The plugin parameter must be an instance of the Bukkit Plugin instance!");
        }

        return new DatabaseManager(plugin);
    }
}
