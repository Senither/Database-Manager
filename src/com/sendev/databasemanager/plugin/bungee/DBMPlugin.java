package com.sendev.databasemanager.plugin.bungee;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.bungee.commands.CommandHandler;
import com.sendev.databasemanager.plugin.bungee.commands.HelpCommand;
import com.sendev.databasemanager.plugin.bungee.commands.PluginsCommand;
import com.sendev.databasemanager.plugin.bungee.commands.VersionCommand;
import com.sendev.databasemanager.plugin.bungee.listeners.NetworkListener;
import com.sendev.databasemanager.plugin.contracts.PlatformType;
import com.sendev.databasemanager.plugin.contracts.PluginContract;
import com.sendev.databasemanager.plugin.utils.BootstrapLogger;
import com.sendev.databasemanager.plugin.utils.ChatFormatter;

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
        BootstrapLogger.logBootMessageTo(getLogger(), getDescription().getVersion());

        // Register commands
        command.registerCommand(new HelpCommand(this));
        command.registerCommand(new VersionCommand(this), true);
        command.registerCommand(new PluginsCommand(this));

        getProxy().getPluginManager().registerCommand(this, command);
        getProxy().getPluginManager().registerListener(this, new NetworkListener(this));
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
