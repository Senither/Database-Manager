package com.sendev.databasemanager.plugin;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.commands.AdminVersionCommand;
import com.sendev.databasemanager.plugin.commands.CommandHandler;
import com.sendev.databasemanager.plugin.commands.DefaultVersionCommand;
import com.sendev.databasemanager.plugin.utils.ChatFormatter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DBMPlugin extends JavaPlugin
{

    private final ChatFormatter chat = new ChatFormatter();
    private final CommandHandler command = new CommandHandler(this);

    private final String prefix = "&%s[&%sDBM&%s]";

    @Override
    public void onEnable()
    {
        command.registerCommand(new AdminVersionCommand(this));
        command.registerCommand(new DefaultVersionCommand(this));

        getCommand("databasemanager").setExecutor(command);
    }

    /**
     * Generates a DBM chat prefix using the provided the colours.
     *
     * @param dark  The dark colour the use
     * @param light The light colour to use
     *
     * @return the generated prefix
     */
    public String getPrefix(char dark, char light)
    {
        return String.format(prefix, dark, light, dark);
    }

    public ChatFormatter getChat()
    {
        return chat;
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
