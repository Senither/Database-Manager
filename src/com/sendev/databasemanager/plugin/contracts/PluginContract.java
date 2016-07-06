package com.sendev.databasemanager.plugin.contracts;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import java.util.logging.Logger;

public interface PluginContract
{

    public Logger getLogger();

    public PlatformType getPlatformType();
    
     /**
     * Gets the database factory instance.
     *
     * @return the database factory instance
     */
    public DatabaseFactory getFactory();

    /**
     * Generates a DBM chat prefix using the provided the colours.
     *
     * @param dark  The dark colour the use
     * @param light The light colour to use
     *
     * @return the generated prefix
     */
    public String getPrefix(char dark, char light);

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
    public DatabaseManager createNewInstance(Object plugin);
}
