package com.sendev.databasemanager.plugin.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatFormatter
{

    /**
     * Colorize a message, using Bukkit/Spigots
     * standard and(&) symbol syntax.
     *
     * @param message The message the colorize.
     *
     * @return String
     */
    public String colorize(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Colorize a list of messages, using Bukkit/Spigots
     * and(&) standard symbol syntax.
     *
     * @param messages The list of messages that should be colorized.
     *
     * @return List
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
     * color code formating and return a plain
     * string with no formating.
     *
     * @param message The message that should be decolorized.
     *
     * @return String
     */
    public String decolorize(String message)
    {
        return ChatColor.stripColor(message);
    }

    /**
     * Decolorizes a list of messages, this will remove all
     * chat color code formating and return an array
     * of plain strings with no formating.
     *
     * @param messages The messages that should be decolorized.
     *
     * @return String
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
     * Send a message to a player or console
     *
     * @param player  Command Sender object (Console)
     * @param message Message to send
     */
    public void sendMessage(CommandSender player, String message)
    {
        player.sendMessage(colorize(message));
    }

    public void sendMessage(CommandSender player, String message, Object... paramaters)
    {
        player.sendMessage(colorize(String.format(message, paramaters)));
    }

    /**
     * Send a message to a player or console
     *
     * @param player  Player object
     * @param message Message to send
     */
    public void sendMessage(Player player, String message)
    {
        player.sendMessage(colorize(message));
    }

    public void sendMessage(Player player, String message, Object... paramaters)
    {
        player.sendMessage(colorize(String.format(message, paramaters)));
    }

    /**
     * Broadcasts a message to all players
     * currently online on the server.
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
     * Broadcasts a message to all players currently online,
     * but only if they have the given permission node.
     *
     * @param message The message that should be broadcasted.
     * @param node    The permission node that the player should have to see the broadcast.
     */
    public void broadcast(String message, String node)
    {
        if (node == null) {
            broadcast(message);
            return;
        }

        message = colorize(message);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasPermission(node)) {
                player.sendMessage(message);
            }
        }
    }
}
