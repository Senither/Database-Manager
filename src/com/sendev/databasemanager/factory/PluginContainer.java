package com.sendev.databasemanager.factory;

import com.sendev.databasemanager.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginContainer
{
    private final String name;
    private final String main;
    private final List<String> binds;
    private final DatabaseManager instance;

    public PluginContainer(Plugin plugin, DatabaseManager instance)
    {
        PluginDescriptionFile description = plugin.getDescription();

        this.name = description.getName();
        this.main = description.getMain();

        this.instance = instance;

        this.binds = new ArrayList<>();
        this.binds.add(this.main);
    }

    public String getName()
    {
        return name;
    }

    public String getMain()
    {
        return main;
    }

    public List<String> getBinds()
    {
        return binds;
    }

    public DatabaseManager getInstance()
    {
        return instance;
    }

    public boolean hasBinding(List<String> origins)
    {
        System.out.println(" === " + name + " ===");
        System.out.println("Binds: " + binds);
        System.out.println("Origins: " + origins);

        return origins.stream().anyMatch(( path ) -> (binds.stream().anyMatch(( sub ) -> (path.equals(sub)))));
    }
}
