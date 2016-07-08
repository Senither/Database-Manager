package com.sendev.databasemanager.plugin.utils.sender;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeSender implements ChatSender
{
    private final ProxiedPlayer player;
    private final CommandSender sender;

    public BungeeSender(ProxiedPlayer player)
    {
        this.player = player;
        this.sender = null;
    }

    public BungeeSender(CommandSender sender)
    {
        this.player = null;
        this.sender = sender;
    }

    @Override
    public void send(String message)
    {
        if (player != null) {
            player.sendMessage(new TextComponent(message));
        }

        if (sender != null) {
            sender.sendMessage(new TextComponent(message));
        }
    }
}
