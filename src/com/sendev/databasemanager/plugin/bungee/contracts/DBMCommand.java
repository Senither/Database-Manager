package com.sendev.databasemanager.plugin.bungee.contracts;

import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.utils.ChatFormatter;
import com.sendev.databasemanager.plugin.utils.sender.BungeeSender;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.CommandSender;

public abstract class DBMCommand
{
    protected final DBMPlugin plugin;

    public DBMCommand(DBMPlugin plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Checks to see if the command has a permission node linked to it.
     *
     * @return checks if the command has a permission node linked to it
     */
    public boolean hasPermission()
    {
        return getPermission() != null;
    }

    /**
     * Gets the permission node linked to the command, if no permission is
     * linked with the command <code>NULL</code> will be returned instead.
     *
     * @return either (1) the permission node linked to the command
     *         or (2) <code>NULL</code>
     */
    public String getPermission()
    {
        return null;
    }

    /**
     * Gets the command triggers.
     *
     * @return the command triggers
     */
    public List<String> getTriggers()
    {
        return new ArrayList<>();
    }

    /**
     * Gets the command description.
     *
     * @return the command description
     */
    public List<String> getDescription()
    {
        return new ArrayList<>();
    }

    /**
     * Gets the command parameters.
     *
     * @return the command parameters
     */
    public List<String> getParameters()
    {
        return new ArrayList<>();
    }

    /**
     * Determines if the command should be displayed on the help menu or not.
     *
     * @return ture if the command should be displayed on the help command
     */
    public boolean displayOnHelp()
    {
        return true;
    }

    /**
     * Sends the command information with the description to the player.
     *
     * @param player the player to send the message to
     *
     * @return ture if the information message was sent successfully.
     */
    public final boolean sendCommandInformationMessage(CommandSender player)
    {
        return sendCommandInformationMessage(player, true);
    }

    /**
     * Sends the command information to the player.
     *
     * @param player          the player to send the message to
     * @param sendDescription if true, the command description will be sent as-well,
     *                        if false the command description will be omitted
     *
     * @return ture if the information message was sent successfully.
     */
    public final boolean sendCommandInformationMessage(CommandSender player, boolean sendDescription)
    {
        if (getTriggers().isEmpty()) {
            return false;
        }

        if (getPermission() != null && !player.hasPermission(getPermission())) {
            return false;
        }

        String command = String.format("&3/&bDBM %s ", getTriggers().get(0));

        if (!getParameters().isEmpty()) {
            command = getParameters().stream().map(( parameter )
            -> String.format("&3[&b%s&3] ", parameter)
            ).reduce(command, String::concat);
        }

        BungeeSender sender = new BungeeSender(player);

        plugin.getChat().sendMessage(sender, command.trim());

        if (sendDescription) {
            getDescription().stream().forEach(( line ) -> {
                plugin.getChat().sendMessage(sender, " &3&l&m*&7 " + line);
            });
        }

        return true;
    }

    /**
     * Sends a prefixed message to the provided sender.
     *
     * @param sender  the sender to send the message to
     * @param message the message to send to the sender
     */
    protected final void sendMessage(CommandSender sender, String message)
    {
        chat().sendMessage(new BungeeSender(sender), "%s &b%s", plugin.getPrefix('3', 'b'), message);
    }

    /**
     * Sends a prefixed error message to the provided player.
     *
     * @param player  the player to send the message to
     * @param message the message to send to the player
     */
    protected final void sendErrorMessage(CommandSender player, String message)
    {
        chat().sendMessage(new BungeeSender(player), "%s &c%s", plugin.getPrefix('4', 'c'), message);
    }

    /**
     * Gets the chat formatter utility.
     *
     * @return the chat formatter
     */
    protected final ChatFormatter chat()
    {
        return plugin.getChat();
    }

    /**
     * Executes the given command, returning its success
     *
     * @param player the player running the command
     * @param args   the argument parsed to the command
     *
     * @return true if a valid command, otherwise false
     */
    public boolean runCommand(CommandSender player, String[] args)
    {
        return false;
    }
}
