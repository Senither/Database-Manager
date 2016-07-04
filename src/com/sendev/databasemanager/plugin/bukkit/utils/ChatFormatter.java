package com.sendev.databasemanager.plugin.bukkit.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatFormatter
{

    /**
     * Colourize a message, using Bukkit/Spigots
     * standard and(&amp;) symbol syntax.
     *
     * @param message The message the colourize.
     *
     * @return the colorized message
     */
    public String colorize(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Colourize a list of messages, using Bukkit/Spigots
     * and(&amp;) standard symbol syntax.
     *
     * @param messages The list of messages that should be colourized.
     *
     * @return the list of colorized messages
     */
    public List<String> colorize(List<String> messages)
    {
        List<String> message = new ArrayList<>();

        messages.stream().filter(( str ) -> !(str == null)).forEach(( str ) -> {
            message.add(colorize(str));
        });

        return message;
    }

    /**
     * Decolorizes a message, this will remove all chat
     * colour code formating and return a plain
     * string with no formating.
     *
     * @param message The message that should be decolourized.
     *
     * @return the decolorized message
     */
    public String decolorize(String message)
    {
        return ChatColor.stripColor(message);
    }

    /**
     * Decolourizes a list of messages, this will remove all
     * chat colour code formating and return an array
     * of plain strings with no formating.
     *
     * @param messages The messages that should be decolourized.
     *
     * @return the list of decolorized messages
     */
    public List<String> decolorize(List<String> messages)
    {
        List<String> message = new ArrayList<>();

        messages.stream().filter(( str ) -> !(str == null)).forEach(( str ) -> {
            message.add(decolorize(str));
        });

        return message;
    }

    /**
     * Sends a missing permission message to the given player.
     *
     * @param player     The player the message should be sent to.
     * @param permission The permission node that should be sent to the player.
     */
    public void missingPermission(Player player, String permission)
    {
        player.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    /**
     * Sends a missing permission message to the given command sender.
     *
     * @param sender     The object the message should be sent to.
     * @param permission The permission node that should be sent to the player.
     */
    public void missingPermission(CommandSender sender, String permission)
    {
        sender.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        sender.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    /**
     * Sends a message to a command sender(terminal/console or player)
     *
     * @param player  Command Sender object (Console)
     * @param message Message to send
     */
    public void sendMessage(CommandSender player, String message)
    {
        player.sendMessage(colorize(message));
    }

    /**
     * Sends a message to a command sender(terminal/console or player)
     *
     * @param sender     The command sender to send the message to
     * @param message    The message to send to the sender
     * @param parameters Any parameters that needs to replace placeholders in the provided message
     */
    public void sendMessage(CommandSender sender, String message, Object... parameters)
    {
        sender.sendMessage(colorize(String.format(message, parameters)));
    }

    /**
     * Send a message to a player.
     *
     * @param player  The player to send the message to
     * @param message The message to send to the player
     */
    public void sendMessage(Player player, String message)
    {
        player.sendMessage(colorize(message));
    }

    /**
     * Send a message to a player.
     *
     * @param player     The player to send the message to
     * @param message    The message to send to the player
     * @param parameters Any parameters that needs to replace placeholders in the provided message
     */
    public void sendMessage(Player player, String message, Object... parameters)
    {
        player.sendMessage(colorize(String.format(message, parameters)));
    }

    /**
     * Broadcasts a message to all players currently online on the server.
     *
     * @param message The message that should be broadcasted
     */
    public void broadcast(String message)
    {
        message = colorize(message);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Broadcasts a message to all players currently online who has the provided permission node.
     *
     * @param message    The message that should be broadcasted.
     * @param permission The permission node that the player should have to see the broadcast.
     */
    public void broadcast(String message, String permission)
    {
        if (permission == null) {
            broadcast(message);
            return;
        }

        message = colorize(message);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
}
