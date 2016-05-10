package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.DatabaseManager;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigurationLoader
{
    protected DatabaseManager plugin;

    public abstract String name();

    public abstract Database load(FileConfiguration file);

    public String path(String path)
    {
        return String.format("connections.%s.%s", name(), path);
    }
}
