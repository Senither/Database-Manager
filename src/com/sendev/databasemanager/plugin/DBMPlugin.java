package com.sendev.databasemanager.plugin;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DBMPlugin extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // This doesn't do anything...
    }

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
    public DatabaseManager createNewInstance(Plugin plugin)
    {
        return DatabaseFactory.createNewInstance(plugin);
    }
}
