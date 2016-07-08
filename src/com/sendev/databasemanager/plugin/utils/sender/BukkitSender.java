package com.sendev.databasemanager.plugin.utils.sender;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSender implements ChatSender
{
    private final Player player;
    private final CommandSender sender;

    public BukkitSender(Player player)
    {
        this.player = player;
        this.sender = null;
    }

    public BukkitSender(CommandSender sender)
    {
        this.sender = sender;
        this.player = null;
    }

    @Override
    public void send(String message)
    {
        if (player != null) {
            player.sendMessage(message);
        }

        if (sender != null) {
            sender.sendMessage(message);
        }
    }
}
