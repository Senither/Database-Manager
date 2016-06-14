package com.sendev.databasemanager.plugin;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.commands.CommandHandler;
import com.sendev.databasemanager.plugin.commands.HelpCommand;
import com.sendev.databasemanager.plugin.commands.VersionCommand;
import com.sendev.databasemanager.plugin.tasks.VersionTask;
import com.sendev.databasemanager.plugin.utils.ChatFormatter;
import com.sendev.databasemanager.plugin.utils.VersionFetcher;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DBMPlugin extends JavaPlugin
{

    private VersionTask version;
    private final ChatFormatter chat = new ChatFormatter();
    private final CommandHandler command = new CommandHandler(this);

    private final String prefix = "&%s[&%sDBM&%s]";

    @Override
    public void onEnable()
    {
        String currentVersion = getDescription().getVersion();
        String latestVersion = currentVersion;

        try {
            latestVersion = VersionFetcher.fetch();
        } catch (IOException ex) {
            getLogger().info("Failed to make a version check with SenDevelopment, the site might be down.");
        }

        version = new VersionTask(this, latestVersion);

        getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        getLogger().log(Level.INFO, "Plugin: Database Manager v{0}", currentVersion);
        getLogger().log(Level.INFO, "Author: Alexis Tan (Senither) ");
        getLogger().log(Level.INFO, "Site: http://sen-dev.com/");
        getLogger().log(Level.INFO, "Wiki: https://bitbucket.org/Senither/database-manager");

        if (!currentVersion.equals(latestVersion)) {
            getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            getLogger().log(Level.INFO, "There is a new version of DBM avaliable!");
            getLogger().log(Level.INFO, "Version avaliable: v{0}", latestVersion);
            getLogger().log(Level.INFO, "Current version:   v{0}", currentVersion);
        }

        getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        command.registerCommand(new HelpCommand(this));
        command.registerCommand(new VersionCommand(this), true);

        getCommand("databasemanager").setExecutor(command);

        version.startTask();
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

    public CommandHandler getCommand()
    {
        return command;
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
