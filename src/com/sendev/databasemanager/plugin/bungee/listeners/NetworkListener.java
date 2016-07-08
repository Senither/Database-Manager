package com.sendev.databasemanager.plugin.bungee.listeners;

import com.sendev.databasemanager.plugin.bungee.DBMPlugin;
import com.sendev.databasemanager.plugin.utils.Version;
import com.sendev.databasemanager.plugin.utils.sender.BungeeSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NetworkListener implements Listener
{
    private final DBMPlugin plugin;
    private final String version;

    public NetworkListener(DBMPlugin plugin)
    {
        this.plugin = plugin;

        this.version = plugin.getDescription().getVersion();
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e)
    {
        ProxiedPlayer player = e.getPlayer();

        if (!player.hasPermission("bungeedatabasemanager.update-notice")) {
            return;
        }

        if (Version.isLatestRelease(version)) {
            return;
        }

        String prefix = plugin.getPrefix('2', 'a');

        BungeeSender sender = new BungeeSender(player);

        plugin.getChat().sendMessage(sender, "%s &7There is a new version of DBM available!", prefix);
        plugin.getChat().sendMessage(sender, "%s &7Server is running &2v%s &7new version is &2v%s", prefix, version, Version.getVersion());
        plugin.getChat().sendMessage(sender, "%s &7Site: &2https://bitbucket.org/Senither/database-manager", prefix);
    }
}
