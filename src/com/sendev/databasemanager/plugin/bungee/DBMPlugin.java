package com.sendev.databasemanager.plugin.bungee;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.bungee.commands.CommandHandler;
import com.sendev.databasemanager.plugin.bungee.commands.HelpCommand;
import com.sendev.databasemanager.plugin.bungee.commands.PluginsCommand;
import com.sendev.databasemanager.plugin.bungee.commands.VersionCommand;
import com.sendev.databasemanager.plugin.bungee.utils.ChatFormatter;
import com.sendev.databasemanager.plugin.contracts.PlatformType;
import com.sendev.databasemanager.plugin.contracts.PluginContract;
import com.sendev.databasemanager.plugin.utils.VersionFetcher;
import java.io.IOException;
import java.util.logging.Level;

public class DBMPlugin extends ProxyPlugin implements PluginContract
{

    private final ChatFormatter chat = new ChatFormatter();
    private final DatabaseFactory factory = new DatabaseFactory();
    private final CommandHandler command = new CommandHandler(this);

    private final String prefix = "&%s[&%sDBM&%s]";

    @Override
    public void onLoad()
    {
        factory.setPlatform(getPlatformType());
    }

    @Override
    public void onEnable()
    {
        // Gets the latest, and current version of DBM
        String currentVersion = getDescription().getVersion();
        String latestVersion = currentVersion;

        try {
            latestVersion = VersionFetcher.fetch();
        } catch (IOException ex) {
            getLogger().info("Failed to make a version check with SenDevelopment, the site might be down.");
        }

        // Send console plugin message
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

        // Register commands
        command.registerCommand(new HelpCommand(this));
        command.registerCommand(new VersionCommand(this), true);
        command.registerCommand(new PluginsCommand(this));

        getProxy().getPluginManager().registerCommand(this, command);
    }

    public ChatFormatter getChat()
    {
        return chat;
    }

    public CommandHandler getCommand()
    {
        return command;
    }

    @Override
    public DatabaseFactory getFactory()
    {
        return factory;
    }

    @Override
    public String getPrefix(char dark, char light)
    {
        return String.format(prefix, dark, light, dark);
    }

    @Override
    public PlatformType getPlatformType()
    {
        return PlatformType.BungeeCord;
    }

    @Override
    public DatabaseManager createNewInstance(Object plugin)
    {
        return DatabaseFactory.createNewInstance(plugin);
    }
}
