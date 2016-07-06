package com.sendev.databasemanager.plugin.bungee.utils;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
     * Broadcasts a message to all players currently online on the server.
     *
     * @param message The message that should be broadcasted
     */
    public void broadcast(String message)
    {
        message = colorize(message);

        for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(message));
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

        for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
}
