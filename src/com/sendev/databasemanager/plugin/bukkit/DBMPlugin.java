package com.sendev.databasemanager.plugin.bukkit;

import com.sendev.databasemanager.DatabaseFactory;
import com.sendev.databasemanager.DatabaseManager;
import com.sendev.databasemanager.plugin.bukkit.commands.CommandHandler;
import com.sendev.databasemanager.plugin.bukkit.commands.HelpCommand;
import com.sendev.databasemanager.plugin.bukkit.commands.PluginsCommand;
import com.sendev.databasemanager.plugin.bukkit.commands.VersionCommand;
import com.sendev.databasemanager.plugin.bukkit.listeners.ServerListener;
import com.sendev.databasemanager.plugin.bukkit.tasks.VersionTask;
import com.sendev.databasemanager.plugin.bukkit.utils.ChatFormatter;
import com.sendev.databasemanager.plugin.contracts.PlatformType;
import com.sendev.databasemanager.plugin.contracts.PluginContract;
import com.sendev.databasemanager.plugin.utils.BootstrapLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class DBMPlugin extends JavaPlugin implements PluginContract
{

    private VersionTask version;
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
//        command.registerCommand(new EditCommand(this));

        getCommand("databasemanager").setExecutor(command);

        // Register events
        getServer().getPluginManager().registerEvents(new ServerListener(this), this);

        // Register tasks
        (version = new VersionTask(this, getDescription().getVersion())).startTask();
    }

    /**
     * Gets the version task instance.
     *
     * @return the version task instance
     */
    public VersionTask getVersion()
    {
        return version;
    }

    /**
     * Gets the chat formatter utility.
     *
     * @return the chat formatter
     */
    public ChatFormatter getChat()
    {
        return chat;
    }

    /**
     * Gets the command handler, the handler will have two lists of commands,
     * one for default(fallback) commands and one for all the other commands.
     *
     * @return the command handler instance
     */
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
        return PlatformType.Bukkit;
    }

    @Override
    public DatabaseManager createNewInstance(Object plugin)
    {
        return DatabaseFactory.createNewInstance(plugin);
    }
}
