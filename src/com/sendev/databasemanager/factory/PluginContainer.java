package com.sendev.databasemanager.factory;

import java.util.ArrayList;
import java.util.List;

import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.contracts.DatabasePlugin;

public class PluginContainer
{
    private final String name;
    private final String main;
    private final List<String> binds;
    private final DatabaseManager instance;

    public PluginContainer(DatabasePlugin plugin, DatabaseManager instance)
    {
        this.name = plugin.getName();
        this.main = plugin.getMain();

        this.instance = instance;

        this.binds = new ArrayList<>();
        this.binds.add(this.main);
    }

    /**
     * Gets the name of the plugin that owns the DBM instance.
     *
     * @return the name of the plugin that owns the DBM instance
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the main class path for the plugin that owns the DBM instance.
     *
     * @return the main class path for the plugin that owns the DBM instance
     */
    public String getMain()
    {
        return main;
    }

    /**
     * Gets the plugin class path bindings.
     *
     * @return the plugin class path bindings
     */
    public List<String> getBinds()
    {
        return binds;
    }

    /**
     * Gets the plugins DBM instance.
     *
     * @return the plugins DBM instance
     */
    public DatabaseManager getInstance()
    {
        return instance;
    }

    /**
     * Checks to see if the provided list of class paths match any of the class path bindings.
     *
     * @param origins the list class paths to match with the bindings
     *
     * @return either (1) true if there is a match with the data bindings
     *         or (2) false if no match was found
     */
    public boolean hasBinding(List<String> origins)
    {
        return origins.stream().anyMatch(( path ) -> (binds.stream().anyMatch(( sub ) -> (path.equals(sub)))));
    }
}
